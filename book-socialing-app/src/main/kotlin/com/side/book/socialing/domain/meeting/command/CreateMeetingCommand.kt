package com.side.book.socialing.domain.meeting.command

import java.time.LocalDateTime

data class CreateMeetingCommand(
    val name: String,
    val description: String?,
    val bookName: String?,
    val bookAuthor: String?,
    val bookLink: String?,
    val meetDate: LocalDateTime,
    val round: Int,
    val createdBy: String
)
