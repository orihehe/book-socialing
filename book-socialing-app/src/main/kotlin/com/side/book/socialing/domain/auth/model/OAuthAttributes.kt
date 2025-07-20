package com.side.book.socialing.domain.auth.model

import com.side.book.socialing.domain.user.entity.User

enum class SocialType {
    KAKAO, NAVER, GOOGLE
}

data class OAuthAttributes(
    val provider: SocialType,
    val attributes: Map<String, Any>,
    val nameAttributeKey: String,
    val name: String,
    val email: String,
    val picture: String,
) {

    fun toEntity(): User {
        return User(
            provider = this.provider.name,
            providerId = this.attributes[this.nameAttributeKey].toString(),
            email = this.email,
            nickname = this.name,
            role = "ROLE_USER"
        )
    }

    companion object {
        fun of(provider: SocialType, userNameAttributeName: String, attributes: Map<String, Any>): OAuthAttributes {
            return when (provider) {
                SocialType.GOOGLE -> ofGoogle(userNameAttributeName, attributes)
                SocialType.NAVER -> ofNaver("response", attributes)
                SocialType.KAKAO -> ofKakao("id", attributes)
            }
        }

        private fun ofGoogle(userNameAttributeName: String, attributes: Map<String, Any>): OAuthAttributes {
            return OAuthAttributes(
                provider = SocialType.GOOGLE,
                name = attributes["name"] as String,
                email = attributes["email"] as String,
                picture = attributes["picture"] as String,
                attributes = attributes,
                nameAttributeKey = userNameAttributeName
            )
        }

        private fun ofNaver(userNameAttributeName: String, attributes: Map<String, Any>): OAuthAttributes {
            val response = attributes["response"] as Map<String, Any>
            return OAuthAttributes(
                provider = SocialType.NAVER,
                name = response["name"] as String,
                email = response["email"] as String,
                picture = response["profile_image"] as String,
                attributes = response,
                nameAttributeKey = userNameAttributeName
            )
        }

        private fun ofKakao(userNameAttributeName: String, attributes: Map<String, Any>): OAuthAttributes {
            val kakaoAccount = attributes["kakao_account"] as Map<String, Any>
            val profile = kakaoAccount["profile"] as Map<String, Any>
            return OAuthAttributes(
                provider = SocialType.KAKAO,
                name = profile["nickname"] as String,
                email = kakaoAccount["email"] as String,
                picture = profile["profile_image_url"] as String,
                attributes = attributes,
                nameAttributeKey = userNameAttributeName
            )
        }
    }
}
