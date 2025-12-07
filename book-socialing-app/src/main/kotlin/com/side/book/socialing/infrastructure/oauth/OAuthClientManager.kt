package com.side.book.socialing.infrastructure.oauth

import com.side.book.socialing.global.utils.log
import org.springframework.stereotype.Component

@Component
class OAuthClientManager(
    private val clients: List<OAuthClient>
) {

    suspend fun unlink(provider: String, socialId: String) {
        val client = clients.find { it.supports(provider) }
        if (client == null) {
            log.warn("No OAuthClient found for provider: {}", provider)
            return
        }

        try {
            client.unlink(socialId)
        } catch (e: Exception) {
            log.error("Error during unlink for provider: {}, socialId: {}", provider, socialId, e)
            throw e
        }
    }
}
