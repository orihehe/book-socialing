package com.side.book.socialing.domain.note.repository

import com.side.book.socialing.domain.note.entity.Note
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDateTime

interface NoteRepository : JpaRepository<Note, Long> {

    @Query("SELECT n FROM Note n JOIN n.participants p WHERE p.userId = :userId AND p.status = 'JOINED' AND n.endDate > :currentDateTime ORDER BY n.endDate")
    fun findActiveNotesByUserId(
        @Param("userId") userId: Long,
        @Param("currentDateTime") currentDateTime: LocalDateTime
    ): List<Note>

    @Query("SELECT n FROM Note n JOIN n.participants p WHERE p.userId = :userId AND p.status = 'JOINED' AND role = 'HOST' AND n.endDate > :currentDateTime ORDER BY n.endDate")
    fun findCreatedNotesByUserId(
        @Param("userId") userId: Long,
        @Param("currentDateTime") currentDateTime: LocalDateTime
    ): List<Note>

    @Query("SELECT n FROM Note n JOIN n.participants p WHERE p.userId = :userId AND p.status = 'PENDING_APPROVAL' AND n.endDate > :currentDateTime ORDER BY p.createdAt")
    fun findPendingNotesByUserId(
        @Param("userId") userId: Long,
        @Param("currentDateTime") currentDateTime: LocalDateTime
    ): List<Note>

    @Query("SELECT n FROM Note n JOIN n.participants p WHERE p.userId = :userId AND p.status <> 'JOINED' AND n.endDate > :currentDateTime ORDER BY RAND() LIMIT 2")
    fun findRecommendNotesByUserId(
        @Param("userId") userId: Long,
        @Param("currentDateTime") currentDateTime: LocalDateTime
    ): List<Note>

    @Query("SELECT n FROM Note n JOIN n.participants p WHERE p.userId = :userId AND p.status = 'JOINED' AND n.endDate <= :currentDateTime ORDER BY n.endDate DESC")
    fun findRevisedNotesByUserId(
        @Param("userId") userId: Long,
        @Param("currentDateTime") currentDateTime: LocalDateTime
    ): List<Note>
}
