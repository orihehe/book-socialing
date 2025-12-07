package com.side.book.socialing.domain.note.service

import com.side.book.socialing.domain.club.service.ClubService
import com.side.book.socialing.domain.note.enum.ParticipantStatus
import com.side.book.socialing.domain.note.repository.NoteParticipantRepository
import com.side.book.socialing.domain.note.repository.NoteRepository
import com.side.book.socialing.domain.user.service.UserService
import com.side.book.socialing.global.error.exception.PermissionDeniedException
import com.side.book.socialing.presentation.note.dto.NoteGuestResponse
import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class NoteHostService(
    private val noteRepository: NoteRepository,
    private val noteParticipantRepository: NoteParticipantRepository,
    private val userService: UserService,
    private val clubService: ClubService
) {

    @Transactional(readOnly = true)
    fun getGuests(userId: Long, noteId: Long): List<NoteGuestResponse> {
        val note = noteRepository.findByIdAndDeletedFalse(noteId) ?: throw EntityNotFoundException("Note $noteId not found")

        if (!note.isHost(userId)) {
            throw PermissionDeniedException("User is not the host of the note")
        }

        val noteParticipants = noteParticipantRepository.findAllByNoteIdAndStatusIn(
            noteId,
            setOf(ParticipantStatus.PENDING_APPROVAL)
        )

        val club = note.club ?: return noteParticipants.filter { !it.isHost() }.map {
            val user = userService.getUser(it.userId)
                ?: throw EntityNotFoundException("User $userId not found")
            NoteGuestResponse(
                user = user,
                role = it.role.toString(),
                status = it.status.toString()
            )
        }
        val clubMemberUserIds = clubService.getClubMemberIds(club.id!!)

        val guestIds = noteParticipants.filter { it.userId !in clubMemberUserIds }.map { it.userId }.toSet()
        val users = userService.getUserMap(guestIds)

        return noteParticipants.map {
            val user = users[it.userId] ?: throw EntityNotFoundException("User $userId not found")
            NoteGuestResponse(
                user = user,
                role = it.role.toString(),
                status = it.status.toString()
            )
        }
    }
}
