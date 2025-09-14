package com.side.book.socialing.domain.note.service

import com.side.book.socialing.domain.note.entity.NoteParticipant
import com.side.book.socialing.domain.note.event.NoteJoinedEvent
import com.side.book.socialing.domain.note.repository.NoteParticipantRepository
import com.side.book.socialing.domain.note.repository.NoteRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.jvm.optionals.getOrNull

// TODO: move logics to Note entity
@Service
class NoteJoinService(
    private val noteRepository: NoteRepository,
    private val noteParticipantRepository: NoteParticipantRepository,
    private val applicationEventPublisher: ApplicationEventPublisher
) {

    @Transactional
    fun joinRequest(userId: Long, noteId: Long) {
        val note = noteRepository.findById(noteId).getOrNull()
            ?: throw EntityNotFoundException("Note $noteId doesn't exist")

        noteParticipantRepository.findByNoteIdAndUserId(noteId, userId)?.let {
            it.joinRequest()
            return
        }

        val participant = NoteParticipant.createMember(
            note = note,
            userId = userId
        )

        noteParticipantRepository.save(participant)
    }

    @Transactional
    fun cancelRequest(userId: Long, noteId: Long) {
        val participant = noteParticipantRepository.findByNoteIdAndUserId(noteId, userId)
            ?: throw EntityNotFoundException("User $userId is not a participant in note $noteId")

        participant.cancel()
    }

    @Transactional
    fun approve(userId: Long, noteId: Long, approvedUserId: Long) {
        val note = noteRepository.findById(noteId).getOrNull()
            ?: throw EntityNotFoundException("Note $noteId doesn't exist")

        if (!note.isHost(userId)) {
            throw IllegalStateException("User $userId is not a host of note ${note.id}")
        }

        val participant = noteParticipantRepository.findByNoteIdAndUserId(noteId, approvedUserId)
            ?: throw EntityNotFoundException("User $approvedUserId is not a participant in note $noteId")

        participant.approve()

        applicationEventPublisher.publishEvent(
            NoteJoinedEvent(
                userId = participant.userId,
                noteId = note.id!!
            )
        )
    }

    @Transactional
    fun reject(userId: Long, noteId: Long, rejectedUserId: Long) {
        val note = noteRepository.findById(noteId).getOrNull()
            ?: throw EntityNotFoundException("Note $noteId doesn't exist")

        if (!note.isHost(userId)) {
            throw IllegalStateException("User $userId is not a host of note ${note.id}")
        }

        val participant = noteParticipantRepository.findByNoteIdAndUserId(noteId, rejectedUserId)
            ?: throw EntityNotFoundException("User $rejectedUserId is not a participant in note $noteId")

        participant.reject()
    }

    @Transactional
    fun kick(userId: Long, noteId: Long, kickedUserId: Long) {
        val note = noteRepository.findById(noteId).getOrNull()
            ?: throw EntityNotFoundException("Note $noteId doesn't exist")

        if (!note.isHost(userId)) {
            throw IllegalStateException("User $userId is not a host of note ${note.id}")
        }

        if (userId == kickedUserId) {
            throw IllegalStateException("Host can't kick themselves")
        }

        val participant = noteParticipantRepository.findByNoteIdAndUserId(noteId, kickedUserId)
            ?: throw EntityNotFoundException("User $kickedUserId is not a participant in note $noteId")

        participant.kick()
    }
}
