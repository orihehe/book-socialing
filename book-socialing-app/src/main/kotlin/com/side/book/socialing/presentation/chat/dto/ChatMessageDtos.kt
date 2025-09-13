package com.side.book.socialing.presentation.chat.dto

import java.time.LocalDateTime

data class ChatMessageRequest(
    val roomId: Long,
    val content: String,
    val type: String
)

data class ChatMessageResponse(
    val messageId: Long,
    val senderNickname: String,
    val content: String,
    val type: String,
    val sentAt: LocalDateTime
)
