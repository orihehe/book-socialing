package com.side.book.socialing.presentation.chat.dto

data class ChatMessageRequest(
    val content: String,
    val type: String,
    val emojis: List<String> = listOf()
)

data class ChatMessageResponse(
    val messageId: Long,
    val senderNickname: String,
    val content: String,
    val type: String,
    val emojis: List<String> = listOf(),
    val sentAt: String
)
