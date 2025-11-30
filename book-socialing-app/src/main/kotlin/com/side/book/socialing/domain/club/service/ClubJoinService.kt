package com.side.book.socialing.domain.club.service

import com.side.book.socialing.domain.club.entity.ClubParticipant
import com.side.book.socialing.domain.club.enum.ParticipantRole
import com.side.book.socialing.domain.club.enum.ParticipantStatus
import com.side.book.socialing.domain.club.repository.ClubParticipantRepository
import com.side.book.socialing.domain.club.repository.ClubRepository
import com.side.book.socialing.domain.user.service.UserService
import com.side.book.socialing.presentation.club.ClubMemberResponse
import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ClubJoinService(
    private val clubRepository: ClubRepository,
    private val clubParticipantRepository: ClubParticipantRepository,
    private val userService: UserService
) {

    @Transactional
    fun joinRequest(userId: Long, clubId: Long) {
        val club = clubRepository.findByIdAndDeletedFalse(clubId)
            ?: throw EntityNotFoundException("Club $clubId doesn't exist")

        clubParticipantRepository.findByClubIdAndUserId(clubId, userId)?.let {
            it.joinRequest()
            return
        }

        val participant = ClubParticipant.create(
            club = club,
            userId = userId,
            role = ParticipantRole.MEMBER,
            status = ParticipantStatus.PENDING_APPROVAL
        )

        clubParticipantRepository.save(participant)
    }

    @Transactional
    fun cancelRequest(userId: Long, clubId: Long) {
        val participant = clubParticipantRepository.findByClubIdAndUserId(clubId, userId)
            ?: throw EntityNotFoundException("User $userId is not a participant in club $clubId")

        participant.cancel()
    }

    @Transactional
    fun approve(userId: Long, clubId: Long, approvedUserId: Long) {
        val club = clubRepository.findByIdAndDeletedFalse(clubId)
            ?: throw EntityNotFoundException("Club $clubId doesn't exist")

        if (!club.isHost(userId)) {
            throw IllegalStateException("User $userId is not a host of club ${club.id}")
        }

        val participant = clubParticipantRepository.findByClubIdAndUserId(clubId, approvedUserId)
            ?: throw EntityNotFoundException("User $approvedUserId is not a participant in club $clubId")

        participant.approve()
    }

    @Transactional
    fun reject(userId: Long, clubId: Long, rejectedUserId: Long) {
        val club = clubRepository.findByIdAndDeletedFalse(clubId)
            ?: throw EntityNotFoundException("Club $clubId doesn't exist")

        if (!club.isHost(userId)) {
            throw IllegalStateException("User $userId is not a host of club ${club.id}")
        }

        val participant = clubParticipantRepository.findByClubIdAndUserId(clubId, rejectedUserId)
            ?: throw EntityNotFoundException("User $rejectedUserId is not a participant in club $clubId")

        participant.reject()
    }

    @Transactional
    fun kick(userId: Long, clubId: Long, kickedUserId: Long) {
        val club = clubRepository.findByIdAndDeletedFalse(clubId)
            ?: throw EntityNotFoundException("Club $clubId doesn't exist")

        if (!club.isHost(userId)) {
            throw IllegalStateException("User $userId is not a host of club ${club.id}")
        }

        if (userId == kickedUserId) {
            throw IllegalStateException("Host can't kick themselves")
        }

        val participant = clubParticipantRepository.findByClubIdAndUserId(clubId, kickedUserId)
            ?: throw EntityNotFoundException("User $kickedUserId is not a participant in club $clubId")

        participant.kick()
    }

    @Transactional(readOnly = true)
    fun getClubMembers(clubId: Long): List<ClubMemberResponse> {
        clubRepository.findByIdAndDeletedFalse(clubId)
            ?: throw EntityNotFoundException("Club $clubId doesn't exist")

        val participantMap = clubParticipantRepository.findAllByClubIdAndStatus(clubId, ParticipantStatus.JOINED)
            .associateBy { it.userId }
        if (participantMap.isEmpty()) {
            throw EntityNotFoundException("No participants found for club $clubId")
        }

        val users = userService.getUserMap(participantMap.keys)

        return users
            .map {
                ClubMemberResponse(
                    user = it.value,
                    role = participantMap[it.key]!!.role.toString(),
                    status = participantMap[it.key]!!.status.toString()
                )
            }
    }
}
