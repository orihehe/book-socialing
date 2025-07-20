package com.side.book.socialing.domain.auth.service

import com.side.book.socialing.domain.auth.model.OAuthAttributes
import com.side.book.socialing.domain.auth.model.SocialType
import com.side.book.socialing.domain.user.entity.User
import com.side.book.socialing.domain.user.repository.UserRepository
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
import org.springframework.security.oauth2.core.user.DefaultOAuth2User
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service

@Service
class CustomOAuth2UserService(
    private val userRepository: UserRepository
) : OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User {
        val delegate = DefaultOAuth2UserService()
        val oAuth2User = delegate.loadUser(userRequest)

        val registrationId = userRequest.clientRegistration.registrationId
        val socialType = getSocialType(registrationId)

        val userNameAttributeName = userRequest.clientRegistration.providerDetails.userInfoEndpoint.userNameAttributeName

        val attributes = OAuthAttributes.of(
            provider = socialType,
            userNameAttributeName = userNameAttributeName,
            attributes = oAuth2User.attributes
        )

        val user = saveOrUpdate(attributes)

        return DefaultOAuth2User(
            setOf(SimpleGrantedAuthority(user.role)),
            oAuth2User.attributes,
            attributes.nameAttributeKey
        )
    }

    private fun getSocialType(registrationId: String): SocialType {
        return when (registrationId.uppercase()) {
            "KAKAO" -> SocialType.KAKAO
            else -> throw IllegalArgumentException("Unsupported Social Type")
        }
    }

    private fun saveOrUpdate(attributes: OAuthAttributes): User {
        var user = userRepository.findByEmail(attributes.email)
        if (user == null) {
            user = userRepository.save(attributes.toEntity())
        }
        return user
    }
}
