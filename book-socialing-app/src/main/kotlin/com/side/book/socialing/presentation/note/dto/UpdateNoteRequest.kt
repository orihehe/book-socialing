package com.side.book.socialing.presentation.note.dto

import java.time.LocalDateTime

data class UpdateNoteRequest(
    val bookName: String,
    val bookAuthor: String,
    val description: String,
    val startAt: LocalDateTime,
    val endAt: LocalDateTime
)
