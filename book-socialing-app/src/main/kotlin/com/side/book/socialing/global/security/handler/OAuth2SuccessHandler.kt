package com.side.book.socialing.global.security.handler

import com.side.book.socialing.global.security.provider.JwtTokenProvider
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component
import org.springframework.web.util.UriComponentsBuilder

@Component
class OAuth2SuccessHandler(
        private val jwtTokenProvider: JwtTokenProvider,
        @Value("\${app.server-url}") private val serverUrl: String
) : AuthenticationSuccessHandler {

        override fun onAuthenticationSuccess(
                request: HttpServletRequest,
                response: HttpServletResponse,
                authentication: Authentication
        ) {
                val oAuth2User = authentication.principal as OAuth2User
                val oAuth2AuthenticationToken = authentication as OAuth2AuthenticationToken

                val registrationId = oAuth2AuthenticationToken.authorizedClientRegistrationId

                val email =
                        when (registrationId) {
                                "kakao" ->
                                        (oAuth2User.attributes["kakao_account"] as
                                                Map<String, Any>)["email"] as
                                                String
                                else ->
                                        throw IllegalStateException(
                                                "Unsupported registrationId: $registrationId"
                                        )
                        }

                val accessToken = jwtTokenProvider.createAccessToken(email)
                val refreshToken = jwtTokenProvider.createRefreshToken()

                // state 파라미터에서 redirect_uri 가져오기, 없으면 기본값
                val redirectUri = request.getParameter("state") ?: "$serverUrl/oauth/callback"

                val targetUrl =
                        UriComponentsBuilder.fromUriString(redirectUri)
                                .queryParam("accessToken", accessToken)
                                .queryParam("refreshToken", refreshToken)
                                .build()
                                .toUriString()

                response.sendRedirect(targetUrl)
        }
}
