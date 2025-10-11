package com.side.book.socialing.global.security.filter

import com.side.book.socialing.global.security.service.AuthService
import com.side.book.socialing.global.utils.log
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

class JwtAuthenticationFilter(
    private val authService: AuthService
) : OncePerRequestFilter() {

    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        val path = request.requestURI
        return path == "/" ||
            path.startsWith("/oauth2/") ||
            path.startsWith("/login/oauth2/code/")
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val token = resolveToken(request)

        if (token != null) {
            try {
                val authentication = authService.getAuthentication(token)
                SecurityContextHolder.getContext().authentication = authentication
            } catch (e: Exception) {
                log.error("JWT 인증 실패 - URI: ${request.requestURI}", e)

                response.status = HttpServletResponse.SC_UNAUTHORIZED
                response.contentType = "application/json"
                response.characterEncoding = "UTF-8"
                response.writer.write("""{"error": "Unauthorized", "message": "${e.message}"}""")
                return
            }
        }

        filterChain.doFilter(request, response)
    }

    private fun resolveToken(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader("Authorization")
        return if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            bearerToken.substring(7)
        } else {
            null
        }
    }
}
