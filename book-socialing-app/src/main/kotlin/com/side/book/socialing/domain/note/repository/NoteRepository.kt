package com.side.book.socialing.domain.note.repository

import com.side.book.socialing.domain.note.dto.SearchNoteDto
import com.side.book.socialing.domain.note.entity.Note
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDateTime

interface NoteRepository : JpaRepository<Note, Long> {

    @Query("SELECT count(DISTINCT n.id) FROM Note n JOIN n.participants p WHERE p.userId = :userId AND p.status = 'JOINED' AND n.endAt > :currentDateTime AND n.deleted = false")
    fun countActiveNotesByUserId(userId: Long, currentDateTime: LocalDateTime): Long

    @Query("SELECT n FROM Note n JOIN n.participants p WHERE p.userId = :userId AND p.status = 'JOINED' AND n.endAt > :currentDateTime AND n.deleted = false")
    fun findActiveNotesByUserId(
        @Param("userId") userId: Long,
        @Param("currentDateTime") currentDateTime: LocalDateTime,
        @Param("pageable") pageable: Pageable
    ): List<Note>

    @Query("SELECT count(DISTINCT n.id) FROM Note n JOIN n.participants p WHERE p.userId = :userId AND p.status = 'JOINED' AND role = 'HOST' AND n.endAt > :currentDateTime AND n.deleted = false")
    fun countCreatedNotesByUserId(userId: Long, currentDateTime: LocalDateTime): Long

    @Query("SELECT n FROM Note n JOIN n.participants p WHERE p.userId = :userId AND p.status = 'JOINED' AND role = 'HOST' AND n.endAt > :currentDateTime AND n.deleted = false")
    fun findCreatedNotesByUserId(
        @Param("userId") userId: Long,
        @Param("currentDateTime") currentDateTime: LocalDateTime,
        @Param("pageable") pageable: Pageable
    ): List<Note>

    @Query("SELECT count(DISTINCT n.id) FROM Note n JOIN n.participants p WHERE p.userId = :userId AND p.status = 'PENDING_APPROVAL' AND n.deleted = false ORDER BY p.createdAt")
    fun countPendingNotesByUserId(userId: Long, currentDateTime: LocalDateTime): Long

    @Query("SELECT n FROM Note n JOIN n.participants p WHERE p.userId = :userId AND p.status = 'PENDING_APPROVAL' AND n.endAt > :currentDateTime AND n.deleted = false")
    fun findPendingNotesByUserId(
        @Param("userId") userId: Long,
        @Param("currentDateTime") currentDateTime: LocalDateTime,
        @Param("pageable") pageable: Pageable
    ): List<Note>

    @Query("SELECT n FROM Note n JOIN n.participants p WHERE p.userId = :userId AND p.status <> 'JOINED' AND n.endAt > :currentDateTime AND n.deleted = false ORDER BY RAND() LIMIT 2")
    fun findRecommendNotesByUserId(
        @Param("userId") userId: Long,
        @Param("currentDateTime") currentDateTime: LocalDateTime
    ): List<Note>

    @Query("SELECT count(DISTINCT n.id) FROM Note n JOIN n.participants p WHERE p.userId = :userId AND p.status = 'JOINED' AND n.endAt <= :currentDateTime AND n.deleted = false")
    fun countRevisedNotesByUserId(userId: Long, currentDateTime: LocalDateTime): Long

    @Query("SELECT n FROM Note n JOIN n.participants p WHERE p.userId = :userId AND p.status = 'JOINED' AND n.endAt <= :currentDateTime AND n.deleted = false")
    fun findRevisedNotesByUserId(
        @Param("userId") userId: Long,
        @Param("currentDateTime") currentDateTime: LocalDateTime,
        @Param("pageable") pageable: Pageable
    ): List<Note>

    @Query(
        """
        SELECT COUNT(DISTINCT n.id) FROM Note n 
        WHERE (n.bookName LIKE :keyword OR :keyword IS NULL)
        AND n.deleted = false
    """
    )
    fun countNoteByBookName(
        @Param("keyword") keyword: String?
    ): Long

    @Query(
        """
        SELECT 
            new com.side.book.socialing.domain.note.dto.SearchNoteDto(
                n,
                CASE WHEN :userId IS NOT NULL AND p.id IS NOT NULL AND p.status = 'JOINED' THEN TRUE ELSE FALSE END AS isJoined,
                CASE WHEN :userId IS NOT NULL AND p.id IS NOT NULL AND p.role = 'HOST' THEN TRUE ELSE FALSE END AS isHost
            )
        FROM Note n 
        JOIN n.participants p
        WHERE (n.bookName LIKE :keyword OR :keyword IS NULL)
        AND n.deleted = false
        """
    )
    fun findNoteByBookName(
        @Param("userId") userId: Long?,
        @Param("keyword") keyword: String?,
        @Param("pageable") pageable: Pageable
    ): List<SearchNoteDto>
}
