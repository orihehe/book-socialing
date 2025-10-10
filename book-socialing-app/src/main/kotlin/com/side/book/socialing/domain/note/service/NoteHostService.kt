package com.side.book.socialing.domain.note.service

import com.side.book.socialing.domain.club.service.ClubService
import com.side.book.socialing.domain.note.repository.NoteParticipantRepository
import com.side.book.socialing.domain.note.repository.NoteRepository
import com.side.book.socialing.domain.user.dto.UserDto
import com.side.book.socialing.domain.user.repository.UserRepository
import com.side.book.socialing.global.exception.PermissionDeniedException
import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Service

@Service
class NoteHostService(
    private val noteRepository: NoteRepository,
    private val noteParticipantRepository: NoteParticipantRepository,
    private val userRepository: UserRepository,
    private val clubService: ClubService
) {

    fun getGuests(userId: Long, noteId: Long): List<UserDto> {
        val note = noteRepository.findById(noteId).orElseThrow { throw EntityNotFoundException("Note not found") }

        if (!note.isHost(userId)) {
            throw PermissionDeniedException("User is not the host of the note")
        }

        val noteParticipants = noteParticipantRepository.findAllByNoteId(noteId)

        val club = note.club ?: return noteParticipants.filter { !it.isHost() }.map {
            val user = userRepository.findById(it.userId).orElseThrow { throw EntityNotFoundException("user not found") }
            UserDto.from(user)
        }
        val clubMemberUserIds = clubService.getClubMemberIds(club.id!!)

        val guestIds = noteParticipants.filter { it.userId !in clubMemberUserIds }.map { it.userId }
        val users = userRepository.findAllById(guestIds).associateBy { it.id }

        return noteParticipants.mapNotNull {
            users[it.userId]?.let { user -> UserDto.from(user) }
        }
    }
}
