package com.side.book.socialing.domain.note.event

data class NoteCreatedEvent(
    val noteId: Long,
    val noteTitle: String,
    val userId: Long
)

data class NoteJoinedEvent(
    val noteId: Long,
    val userId: Long
)
