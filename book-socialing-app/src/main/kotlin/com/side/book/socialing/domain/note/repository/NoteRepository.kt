package com.side.book.socialing.domain.note.repository

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
}
