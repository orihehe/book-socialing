package com.side.book.socialing.domain.note.service

import com.side.book.socialing.domain.club.repository.ClubParticipantRepository
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
    private val clubParticipantRepository: ClubParticipantRepository,
    private val userService: UserService
) {

    @Transactional(readOnly = true)
    fun getGuests(userId: Long, noteId: Long): List<NoteGuestResponse> {
        val note = noteRepository.findByIdAndDeletedFalse(noteId)
            ?: throw EntityNotFoundException("Note $noteId not found")

        if (!note.isHost(userId)) {
            throw PermissionDeniedException("User is not the host of the note")
        }

        val explicitParticipants = noteParticipantRepository.findAllByNoteIdAndStatusIn(
            noteId,
            setOf(ParticipantStatus.JOINED, ParticipantStatus.PENDING_APPROVAL)
        )

        val implicitClubMembers = if (note.club != null) {
            val explicitUserIds = explicitParticipants.map { it.userId }.toSet()

            clubParticipantRepository.findAllByClubIdAndStatusIn(note.club!!.id!!, setOf(com.side.book.socialing.domain.club.enum.ParticipantStatus.JOINED))
                .filter { it.userId !in explicitUserIds }
        } else {
            emptyList()
        }

        val allUserIds = explicitParticipants.map { it.userId } + implicitClubMembers.map { it.userId }
        val userMap = userService.getUserMap(allUserIds.toSet())

        val responseList = mutableListOf<NoteGuestResponse>()

        explicitParticipants
            .forEach { part ->
                userMap[part.userId]?.let { user ->
                    responseList.add(
                        NoteGuestResponse(
                            user = user,
                            role = part.role.toString(),
                            status = part.status.toString()
                        )
                    )
                }
            }

        implicitClubMembers.forEach { part ->
            userMap[part.userId]?.let { user ->
                responseList.add(
                    NoteGuestResponse(
                        user = user,
                        role = "MEMBER",
                        status = "JOINED"
                    )
                )
            }
        }

        return responseList
    }
}
