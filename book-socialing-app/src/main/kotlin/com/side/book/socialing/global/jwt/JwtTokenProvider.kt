package com.side.book.socialing.global.jwt

import com.side.book.socialing.global.utils.log
import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.security.Key
import java.util.*

@Component
class JwtTokenProvider(
    @Value("\${jwt.secret}") secret: String,
    @Value("\${jwt.access-token-validity-in-seconds}") accessTokenValidity: Long,
    @Value("\${jwt.refresh-token-validity-in-seconds}") refreshTokenValidity: Long
) {

    private val secretKey: Key = Keys.hmacShaKeyFor(secret.toByteArray(StandardCharsets.UTF_8))
    private val accessTokenValidityInMilliseconds: Long = accessTokenValidity * 1000
    private val refreshTokenValidityInMilliseconds: Long = refreshTokenValidity * 1000

    fun createAccessToken(userEmail: String): String {
        return createToken(userEmail, accessTokenValidityInMilliseconds)
    }

    fun createRefreshToken(): String {
        return createToken(null, refreshTokenValidityInMilliseconds)
    }

    private fun createToken(subject: String?, validity: Long): String {
        val now = Date()
        val expiryDate = Date(now.time + validity)

        return Jwts.builder()
            .apply { if (subject != null) { setSubject(subject) } }
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(secretKey, SignatureAlgorithm.HS256)
            .compact()
    }

    fun getUserEmailFromToken(token: String): String {
        return getClaims(token).subject
    }

    fun validateToken(token: String): Boolean {
        try {
            getClaims(token)
            return true
        } catch (e: Exception) {
            when (e) {
                is io.jsonwebtoken.security.SecurityException, is MalformedJwtException -> log.error("잘못된 JWT 서명입니다.")
                is ExpiredJwtException -> log.error("만료된 JWT 토큰입니다.")
                is UnsupportedJwtException -> log.error("지원되지 않는 JWT 토큰입니다.")
                is IllegalArgumentException -> log.error("JWT 토큰이 잘못되었습니다.")
                else -> log.error("알 수 없는 JWT 오류가 발생했습니다.", e)
            }
        }
        return false
    }

    private fun getClaims(token: String): Claims {
        return Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .body
    }
}
