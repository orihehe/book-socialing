package com.side.book.socialing.presentation.meeting.dto

import java.time.LocalDateTime

data class CreateMeetingRequest(
    val name: String,
    val description: String?,
    val bookName: String?,
    val bookAuthor: String?,
    val bookLink: String?,
    val meetDate: LocalDateTime,
    val round: Int
)
