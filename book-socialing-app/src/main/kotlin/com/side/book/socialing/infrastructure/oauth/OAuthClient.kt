package com.side.book.socialing.infrastructure.oauth

interface OAuthClient {

    fun supports(provider: String): Boolean

    suspend fun unlink(socialId: String)
}
