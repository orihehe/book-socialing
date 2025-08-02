package com.side.book.socialing.global.security.service

import com.side.book.socialing.domain.user.repository.UserRepository
import com.side.book.socialing.global.security.exception.JwtAuthenticationException
import com.side.book.socialing.global.security.provider.JwtTokenProvider
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Service

@Service
@ConditionalOnProperty(name = ["spring.auth.active"], havingValue = "true")
class JwtAuthService(
    private val jwtTokenProvider: JwtTokenProvider,
    private val userRepository: UserRepository
) : AuthService {
    /**
     * JWT 토큰을 받아 유효성을 검증하고 Authentication 객체를 반환합니다.
     * @param token Bearer 접두사를 제외한 순수 토큰
     * @return 유효한 경우 Authentication 객체
     * @throws JwtAuthenticationException 유효하지 않은 토큰이거나 사용자를 찾을 수 없는 경우
     */
    override fun getAuthentication(token: String?): Authentication {
        if (token.isNullOrBlank()) {
            throw JwtAuthenticationException("인증 토큰이 없습니다.")
        }

        if (!jwtTokenProvider.validateToken(token)) {
            throw JwtAuthenticationException("유효하지 않은 JWT 토큰입니다.")
        }

        val email = jwtTokenProvider.getUserEmailFromToken(token)

        val user = userRepository.findByEmail(email)
            ?: throw JwtAuthenticationException("해당 토큰으로 사용자를 찾을 수 없습니다.")

        return UsernamePasswordAuthenticationToken(
            user,
            null,
            setOf(SimpleGrantedAuthority(user.role))
        )
    }
}
