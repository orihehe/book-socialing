package com.side.book.socialing.presentation.note.dto

import java.time.LocalDateTime

data class CreateNoteRequest(
    val bookName: String?,
    val bookAuthor: String?,
    val description: String?,
    val startDate: LocalDateTime,
    val endDate: LocalDateTime
)
