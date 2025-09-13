package com.side.book.socialing.domain.note.event

data class NoteCreatedEvent(
    val noteId: Long,
    val noteTitle: String,
)
