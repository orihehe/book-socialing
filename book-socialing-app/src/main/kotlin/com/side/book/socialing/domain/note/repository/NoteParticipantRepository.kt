package com.side.book.socialing.domain.note.repository

import com.side.book.socialing.domain.note.entity.NoteParticipant
import org.springframework.data.jpa.repository.JpaRepository

interface NoteParticipantRepository : JpaRepository<NoteParticipant, Long> {
    fun findByNoteIdAndUserId(noteId: Long, userId: Long): NoteParticipant?

    fun findAllByNoteId(noteId: Long): List<NoteParticipant>
}
