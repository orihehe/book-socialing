package com.side.book.socialing.domain.note.repository

import com.side.book.socialing.domain.note.dto.SearchNoteDto
import com.side.book.socialing.domain.note.entity.Note
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDateTime

interface NoteRepository : JpaRepository<Note, Long> {
    fun findByIdAndDeletedFalse(noteId: Long?): Note?

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

    @Query("SELECT count(DISTINCT n.id) FROM Note n JOIN n.participants p WHERE p.userId = :userId AND p.status = 'PENDING_APPROVAL' AND n.deleted = false")
    fun countPendingNotesByUserId(userId: Long, currentDateTime: LocalDateTime): Long

    @Query("SELECT n FROM Note n JOIN n.participants p WHERE p.userId = :userId AND p.status = 'PENDING_APPROVAL' AND n.endAt > :currentDateTime AND n.deleted = false ORDER BY p.createdAt")
    fun findPendingNotesByUserId(
        @Param("userId") userId: Long,
        @Param("currentDateTime") currentDateTime: LocalDateTime,
        @Param("pageable") pageable: Pageable
    ): List<Note>

    @Query("SELECT count(DISTINCT n.id) FROM Note n JOIN n.participants p WHERE NOT EXISTS ( SELECT 1 FROM n.participants p WHERE p.userId = :userId) AND n.endAt > :currentDateTime AND n.deleted = false")
    fun countRecommendNotesByUserId(userId: Long, currentDateTime: LocalDateTime): Long

    @Query("SELECT n FROM Note n JOIN n.participants p WHERE NOT EXISTS ( SELECT 1 FROM n.participants p WHERE p.userId = :userId) AND n.endAt > :currentDateTime AND n.deleted = false ORDER BY RAND()")
    fun findRecommendNotesByUserId(
        @Param("userId") userId: Long,
        @Param("currentDateTime") currentDateTime: LocalDateTime,
        @Param("pageable") pageable: Pageable
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
                CAST(p.status AS string),
                CAST(p.role AS string)
            )
        FROM Note n 
        LEFT JOIN n.participants p WITH p.userId = :userId
        WHERE (n.bookName LIKE :keyword OR :keyword IS NULL)
        AND n.deleted = false
        """
    )
    fun findNoteByBookName(
        @Param("userId") userId: Long?,
        @Param("keyword") keyword: String?,
        @Param("pageable") pageable: Pageable
    ): List<SearchNoteDto>

    @Query(
        """
        SELECT 
            n
        FROM Note n 
        LEFT JOIN n.participants p WITH p.userId = :userId
        WHERE p.userId = :userId 
        AND n.deleted = false
        AND (
            (:dateType = 'START' AND (:startDate IS NULL OR n.startAt >= :startDate)) OR
            (:dateType = 'END' AND (:startDate IS NULL OR n.endAt >= :startDate))
        )
        AND (
            (:dateType = 'START' AND (:endDate IS NULL OR n.startAt <= :endDate)) OR
            (:dateType = 'END' AND (:endDate IS NULL OR n.endAt <= :endDate))
        )
        ORDER BY 
            CASE 
                WHEN :dateType = 'START' THEN n.startAt
                WHEN :dateType = 'END' THEN n.endAt
                ELSE n.createdAt
            END DESC
        """
    )
    fun findParticipatedNotesByUserId(
        @Param("userId") userId: Long?,
        @Param("dateType") dateType: String,
        @Param("startDate") startDate: LocalDateTime?,
        @Param("endDate") endDate: LocalDateTime?
    ): List<Note>
}
