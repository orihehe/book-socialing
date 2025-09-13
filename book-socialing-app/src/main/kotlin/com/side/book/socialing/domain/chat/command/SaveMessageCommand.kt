package com.side.book.socialing.domain.chat.command

data class SaveMessageCommand(
    val roomId: Long,
    val senderId: Long,
    val content: String,
    val messageType: String
)
