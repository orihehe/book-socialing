package com.side.book.socialing.domain.club.repository

import com.side.book.socialing.domain.club.entity.Club
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.Optional

interface ClubRepository : JpaRepository<Club, Long> {

    fun findById(clubId: Long?): Optional<Club>

    @Query("SELECT c FROM Club c JOIN c.participants p WHERE p.userId = :userId AND p.status = 'JOINED' ORDER BY c.createdAt")
    fun findJoinedClubByUserId(userId: Long): List<Club>

    @Query("SELECT c FROM Club c JOIN c.participants p WHERE p.userId = :userId AND p.status = 'JOINED' AND p.role = 'HOST' ORDER BY c.createdAt")
    fun findCreatedClubsByUserId(userId: Long): List<Club>

    @Query("SELECT c FROM Club c JOIN c.participants p WHERE p.userId = :userId AND p.status = 'PENDING_APPROVAL' ORDER BY p.createdAt")
    fun findPendingClubsByUserId(userId: Long): List<Club>

    @Query("SELECT c FROM Club c JOIN c.participants p WHERE p.userId = :userId AND p.status <> 'JOINED' ORDER BY RAND() LIMIT 2")
    fun findRecommendClubsByUserId(userId: Long): List<Club>

}
