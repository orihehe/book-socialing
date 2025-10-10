package com.side.book.socialing.domain.club.repository

import com.side.book.socialing.domain.club.entity.ClubParticipant
import org.springframework.data.jpa.repository.JpaRepository

interface ClubParticipantRepository : JpaRepository<ClubParticipant, Long> {
    fun findAllByClubId(clubId: Long): List<ClubParticipant>
}
