package com.side.book.socialing.domain.note.dto

import com.side.book.socialing.domain.note.entity.Note

data class SearchNoteDto(
    val note: Note,
    val status: String,
    val role: String
)
