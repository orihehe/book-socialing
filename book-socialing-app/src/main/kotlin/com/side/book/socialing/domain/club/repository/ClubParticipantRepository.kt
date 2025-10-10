package com.side.book.socialing.domain.club.repository

import com.side.book.socialing.domain.club.entity.ClubParticipant
import com.side.book.socialing.domain.club.enum.ParticipantStatus
import org.springframework.data.jpa.repository.JpaRepository

interface ClubParticipantRepository : JpaRepository<ClubParticipant, Long> {
    fun findAllByClubId(clubId: Long): List<ClubParticipant>
    fun findByClubIdAndUserId(clubId: Long, userId: Long): ClubParticipant?
    fun findAllByClubIdAndStatus(clubId: Long, status: ParticipantStatus): List<ClubParticipant>
}
