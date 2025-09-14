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

        if (noteParticipantRepository.findByNoteIdAndUserId(noteId, userId) != null) {
            throw IllegalStateException("User $userId is already a participant in note $noteId")
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
    fun approve(userId: Long, participantId: Long) {
        val participant = noteParticipantRepository.findById(participantId).getOrNull()
            ?: throw EntityNotFoundException("Participant $participantId doesn't exist")

        val note = participant.note
        if (!note.isHost(userId)) {
            throw IllegalStateException("User $userId is not a host of note ${note.id}")
        }
        participant.approve()

        applicationEventPublisher.publishEvent(
            NoteJoinedEvent(
                userId = participant.userId,
                noteId = note.id!!
            )
        )
    }

    @Transactional
    fun reject(userId: Long, participantId: Long) {
        val participant = noteParticipantRepository.findById(participantId).getOrNull()
            ?: throw EntityNotFoundException("Participant $participantId doesn't exist")

        val note = participant.note
        if (!note.isHost(userId)) {
            throw IllegalStateException("User $userId is not a host of note ${note.id}")
        }

        participant.reject()
    }
}
