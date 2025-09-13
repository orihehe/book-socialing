package com.side.book.socialing.domain.chat.dto

data class ChatMessagesResponse(
    val messages: List<SavedMessageDto>,
    val hasNext: Boolean
)
