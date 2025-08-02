package com.side.book.socialing.global.security.handler

import com.side.book.socialing.global.security.service.AuthService
import com.side.book.socialing.global.security.exception.JwtAuthenticationException
import com.side.book.socialing.global.utils.log
import org.springframework.messaging.Message
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.simp.stomp.StompCommand
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.messaging.support.ChannelInterceptor
import org.springframework.messaging.support.MessageHeaderAccessor
import org.springframework.stereotype.Component

@Component
class StompHandler(
    private val authService: AuthService
) : ChannelInterceptor {

    override fun preSend(message: Message<*>, channel: MessageChannel): Message<*>? {
        val accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor::class.java)!!

        if (accessor.command == StompCommand.CONNECT) {
            val bearerToken = accessor.getFirstNativeHeader("Authorization")
            val token = resolveToken(bearerToken)

            try {
                token?.let {
                    val authentication = authService.getAuthentication(it)
                    accessor.user = authentication
                }
            } catch (e: JwtAuthenticationException) {
                log.error("WebSocket 연결 인증 실패: ${e.message}", e)
                return null
            }
        }
        return message
    }

    private fun resolveToken(bearerToken: String?): String? {
        return if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            bearerToken.substring(7)
        } else {
            null
        }
    }
}
