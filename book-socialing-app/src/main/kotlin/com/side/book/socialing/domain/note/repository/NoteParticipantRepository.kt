package com.side.book.socialing.domain.note.repository

import com.side.book.socialing.domain.note.entity.NoteParticipant
import com.side.book.socialing.domain.note.enum.ParticipantStatus
import org.springframework.data.jpa.repository.JpaRepository

interface NoteParticipantRepository : JpaRepository<NoteParticipant, Long> {
    fun findByNoteIdAndUserId(noteId: Long, userId: Long): NoteParticipant?
    fun findAllByNoteId(noteId: Long): List<NoteParticipant>
    fun findAllByUserId(userId: Long): List<NoteParticipant>
    fun existsByNoteIdAndUserIdAndStatus(noteId: Long, userId: Long, status: ParticipantStatus): Boolean
}
