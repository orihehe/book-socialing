package com.side.book.socialing.domain.club.repository

import com.side.book.socialing.domain.club.dto.SearchClubDto
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

    @Query("SELECT count(DISTINCT c.id) FROM Club c JOIN c.participants p WHERE NOT EXISTS ( SELECT 1 FROM c.participants p WHERE p.userId = :userId) AND c.deleted = false")
    fun countRecommendClubsByUserId(userId: Long): Long

    @Query("SELECT c FROM Club c JOIN c.participants p WHERE NOT EXISTS ( SELECT 1 FROM c.participants p WHERE p.userId = :userId) AND c.deleted = false ORDER BY RAND()")
    fun findRecommendClubsByUserId(
        @Param("userId") userId: Long,
        @Param("pageable") pageable: Pageable
    ): List<Club>

    @Query(
        """
        SELECT COUNT(DISTINCT c.id) FROM Club c 
        WHERE (c.clubName LIKE %:keyword% OR c.description LIKE %:keyword% OR :keyword IS NULL)
    """
    )
    fun countSearchClubByClubName(
        @Param("keyword") keyword: String?
    ): Long

    @Query(
        """
        SELECT new com.side.book.socialing.domain.club.dto.SearchClubDto(
            c,
            CASE WHEN :userId IS NOT NULL AND p.id IS NOT NULL AND p.status = 'JOINED' THEN TRUE ELSE FALSE END AS isJoined,
            CASE WHEN :userId IS NOT NULL AND p.id IS NOT NULL AND p.role = 'HOST' THEN TRUE ELSE FALSE END AS isHost
        )
        FROM Club c
        JOIN c.participants p
        WHERE (c.clubName LIKE %:keyword% OR c.description LIKE %:keyword%) OR (:keyword IS NULL)
        AND c.deleted = false
    """
    )
    fun findSearchClubByClubName(
        @Param("userId") userId: Long?,
        @Param("keyword") keyword: String?,
        @Param("pageable") pageable: Pageable
    ): List<SearchClubDto>
}
