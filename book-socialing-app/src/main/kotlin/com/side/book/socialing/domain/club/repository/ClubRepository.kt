package com.side.book.socialing.domain.club.repository

import com.side.book.socialing.domain.club.entity.Club
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.Optional

interface ClubRepository : JpaRepository<Club, Long> {

    fun findById(clubId: Long?): Optional<Club>

    @Query("SELECT count(DISTINCT c.id) FROM Club c JOIN c.participants p WHERE p.userId = :userId AND p.status = 'JOINED'")
    fun countJoinedClubByUserId(userId: Long): Long

    @Query("SELECT c FROM Club c JOIN c.participants p WHERE p.userId = :userId AND p.status = 'JOINED'")
    fun findJoinedClubByUserId(
        @Param("userId") userId: Long,
        @Param("pageable") pageable: Pageable
    ): List<Club>

    @Query("SELECT count(DISTINCT c.id) FROM Club c JOIN c.participants p WHERE p.userId = :userId AND p.status = 'JOINED' AND p.role = 'HOST'")
    fun countCreatedClubsByUserId(userId: Long): Long

    @Query("SELECT c FROM Club c JOIN c.participants p WHERE p.userId = :userId AND p.status = 'JOINED' AND p.role = 'HOST'")
    fun findCreatedClubsByUserId(
        @Param("userId") userId: Long,
        @Param("pageable") pageable: Pageable
    ): List<Club>

    @Query("SELECT count(DISTINCT c.id) FROM Club c JOIN c.participants p WHERE p.userId = :userId AND p.status = 'PENDING_APPROVAL'")
    fun countPendingClubsByUserId(userId: Long): Long

    @Query("SELECT c FROM Club c JOIN c.participants p WHERE p.userId = :userId AND p.status = 'PENDING_APPROVAL'")
    fun findPendingClubsByUserId(
        @Param("userId") userId: Long,
        @Param("pageable") pageable: Pageable
    ): List<Club>

    @Query("SELECT c FROM Club c JOIN c.participants p WHERE p.userId = :userId AND p.status <> 'JOINED' ORDER BY RAND() LIMIT 2")
    fun findRecommendClubsByUserId(userId: Long): List<Club>
}
