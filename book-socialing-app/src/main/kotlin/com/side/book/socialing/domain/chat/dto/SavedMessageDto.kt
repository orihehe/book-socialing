package com.side.book.socialing.domain.chat.dto

import java.time.LocalDateTime

data class SavedMessageDto(
    val messageId: Long,
    val userId: Long,
    val content: String,
    val messageType: String,
    val sentAt: LocalDateTime
)
