package com.side.book.socialing.domain.auth.model

import com.side.book.socialing.domain.user.entity.User

enum class SocialType {
    KAKAO
}

data class OAuthAttributes(
    val provider: SocialType,
    val attributes: Map<String, Any>,
    val nameAttributeKey: String,
    val name: String,
    val email: String,
    val picture: String
) {

    fun toEntity(nickname: String): User {
        return User(
            provider = this.provider.name,
            providerId = this.attributes[this.nameAttributeKey].toString(),
            email = this.email,
            nickname = nickname,
            role = "ROLE_USER"
        )
    }

    companion object {
        fun of(provider: SocialType, userNameAttributeName: String, attributes: Map<String, Any>): OAuthAttributes {
            return when (provider) {
                SocialType.KAKAO -> ofKakao("id", attributes)
            }
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
