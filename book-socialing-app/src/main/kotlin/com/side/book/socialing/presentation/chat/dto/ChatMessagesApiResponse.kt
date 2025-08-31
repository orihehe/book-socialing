package com.side.book.socialing.presentation.chat.dto

data class ChatMessagesApiResponse(
    val messages: List<ChatMessageResponse>,
    val hasNext: Boolean
)
