package com.side.book.socialing.global.security.provider

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
            .apply {
                if (subject != null) {
                    setSubject(subject)
                }
            }
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(secretKey, SignatureAlgorithm.HS256)
            .compact()
    }

    fun getUserEmailFromToken(token: String): String {
        return getClaims(token).subject
    }

    fun validateToken(token: String): Boolean {
        log.info("validateToken 호출됨")
        try {
            val claims = getClaims(token)
            log.info("sub: ${claims.subject}, exp: ${claims.expiration}, roles: ${claims["roles"]}")
            return true
        } catch (e: Exception) {
            log.error("jwt error", e)
            when (e) {
                is io.jsonwebtoken.security.SecurityException, is MalformedJwtException -> log.error("Invalid JWT signature.", e)
                is ExpiredJwtException -> log.error("Expired JWT token.", e)
                is UnsupportedJwtException -> log.error("Unsupported JWT token.", e)
                is IllegalArgumentException -> log.error("JWT token is invalid.", e)
                else -> log.error("An unknown JWT error occurred.", e)
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
