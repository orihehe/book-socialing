package com.side.book.socialing.infrastructure.oauth.kakao

import com.side.book.socialing.global.utils.log
import com.side.book.socialing.infrastructure.oauth.OAuthClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody

@Component
class KakaoAuthClient(
    private val webClient: WebClient,
    @Value("\${kakao.admin-key}") private val kakaoAdminKey: String
) : OAuthClient {

    override fun supports(provider: String): Boolean {
        return "kakao".equals(provider, ignoreCase = true)
    }

    override suspend fun unlink(socialId: String) {
        try {
            val response = webClient.post()
                .uri("https://kapi.kakao.com/v1/user/unlink")
                .header(HttpHeaders.AUTHORIZATION, "KakaoAK $kakaoAdminKey")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(
                    BodyInserters.fromFormData("target_id_type", "user_id")
                        .with("target_id", socialId)
                )
                .retrieve()
                .awaitBody<KakaoUnlinkResponse>()

            log.info("Successfully unlinked user from Kakao. socialId: {}, unlinked_id: {}", socialId, response.id)
        } catch (e: Exception) {
            log.error("Failed to unlink user from Kakao. socialId: {}", socialId, e)
            throw IllegalStateException("Failed to unlink from Kakao.", e)
        }
    }

    private data class KakaoUnlinkResponse(
        val id: Long
    )
}
