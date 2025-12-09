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

    @Query(
        """
        SELECT count(DISTINCT n.id) 
        FROM Note n 
        LEFT JOIN n.participants np
        LEFT JOIN n.club c
        LEFT JOIN c.participants cp
        WHERE n.endAt > :currentDateTime 
          AND n.deleted = false
          AND (
              (np.userId = :userId AND np.status = 'JOINED' AND np.deleted = false)
              OR 
              (cp.userId = :userId AND cp.status = 'JOINED' AND cp.deleted = false)
          )
    """
    )
    fun countActiveNotesByUserId(userId: Long, currentDateTime: LocalDateTime): Long

    @Query(
        """
        SELECT DISTINCT n
        FROM Note n 
        LEFT JOIN n.participants np
        LEFT JOIN n.club c
        LEFT JOIN c.participants cp
        WHERE n.endAt > :currentDateTime 
          AND n.deleted = false
          AND (
              (np.userId = :userId AND np.status = 'JOINED' AND np.deleted = false)
              OR 
              (cp.userId = :userId AND cp.status = 'JOINED' AND cp.deleted = false)
          )
        ORDER BY n.startAt ASC
    """
    )
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

    @Query("SELECT count(DISTINCT n.id) FROM Note n JOIN n.participants p WHERE p.userId = :userId AND p.status = 'PENDING_APPROVAL' AND n.endAt > NOW() AND n.deleted = false")
    fun countPendingNotesByUserId(userId: Long, currentDateTime: LocalDateTime): Long

    @Query("SELECT n FROM Note n JOIN n.participants p WHERE p.userId = :userId AND p.status = 'PENDING_APPROVAL' AND n.endAt > :currentDateTime AND n.deleted = false ORDER BY p.createdAt")
    fun findPendingNotesByUserId(
        @Param("userId") userId: Long,
        @Param("currentDateTime") currentDateTime: LocalDateTime,
        @Param("pageable") pageable: Pageable
    ): List<Note>

    @Query(
        """
        SELECT count(n) 
        FROM Note n 
        WHERE n.club IS NULL        
          AND n.endAt > :currentDateTime 
          AND n.deleted = false
          AND NOT EXISTS (          
              SELECT 1 
              FROM n.participants p 
              WHERE p.userId = :userId
          )
    """
    )
    fun countRecommendNotesByUserId(userId: Long, currentDateTime: LocalDateTime): Long

    @Query(
        """
        SELECT n 
        FROM Note n 
        WHERE n.club IS NULL     
          AND n.endAt > :currentDateTime 
          AND n.deleted = false
          AND NOT EXISTS (       
              SELECT 1 
              FROM n.participants p 
              WHERE p.userId = :userId
          )
        ORDER BY RAND()        
    """
    )
    fun findRecommendNotesByUserId(
        @Param("userId") userId: Long,
        @Param("currentDateTime") currentDateTime: LocalDateTime,
        @Param("pageable") pageable: Pageable
    ): List<Note>

    @Query(
        """
        SELECT count(DISTINCT n.id) 
        FROM Note n 
        LEFT JOIN n.participants np 
        LEFT JOIN n.club c          
        LEFT JOIN c.participants cp 
        WHERE n.endAt <= :currentDateTime
          AND n.deleted = false
          AND (
              (np.userId = :userId AND np.status = 'JOINED' AND np.deleted = false)
              OR 
              (cp.userId = :userId AND cp.status = 'JOINED' AND cp.deleted = false)
          )
    """
    )
    fun countRevisedNotesByUserId(userId: Long, currentDateTime: LocalDateTime): Long

    @Query(
        """
        SELECT DISTINCT n
        FROM Note n 
        LEFT JOIN n.participants np 
        LEFT JOIN n.club c 
        LEFT JOIN c.participants cp 
        WHERE n.endAt <= :currentDateTime
          AND n.deleted = false
          AND (
              (np.userId = :userId AND np.status = 'JOINED' AND np.deleted = false)
              OR 
              (cp.userId = :userId AND cp.status = 'JOINED' AND cp.deleted = false)
          )
        ORDER BY n.endAt DESC
    """
    )
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
            CASE 
                WHEN p.status IS NOT NULL THEN CAST(p.status AS string)
                WHEN (cp.status = 'JOINED' AND cp.deleted = false) THEN 'JOINED'
                ELSE NULL 
            END,
            CASE 
                WHEN p.role IS NOT NULL THEN CAST(p.role AS string)
                WHEN (cp.status = 'JOINED' AND cp.role = 'HOST' AND cp.deleted = false) THEN 'HOST'
                WHEN (cp.status = 'JOINED' AND cp.deleted = false) THEN 'MEMBER'
                ELSE NULL 
            END
        )
        FROM Note n 
        LEFT JOIN n.participants p WITH p.userId = :userId    
        LEFT JOIN n.club c                                    
        LEFT JOIN c.participants cp WITH cp.userId = :userId  
        WHERE (n.bookName LIKE %:keyword% OR :keyword IS NULL)
        AND n.deleted = false
        ORDER BY n.createdAt DESC
    """
    )
    fun findNoteByBookName(
        @Param("userId") userId: Long?,
        @Param("keyword") keyword: String?,
        @Param("pageable") pageable: Pageable
    ): List<SearchNoteDto>

    @Query(
        """
        SELECT DISTINCT n
        FROM Note n 
        LEFT JOIN n.participants np  
        LEFT JOIN n.club c           
        LEFT JOIN c.participants cp  
        WHERE n.deleted = false
        AND (
            (np.userId = :userId AND np.status = 'JOINED' AND np.deleted = false)
            OR 
            (cp.userId = :userId AND cp.status = 'JOINED' AND cp.deleted = false)
        )
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

    @Query(
        """
        SELECT COUNT(n) > 0 
        FROM Note n 
        LEFT JOIN n.participants np 
        LEFT JOIN n.club c 
        LEFT JOIN c.participants cp 
        WHERE n.id = :noteId 
          AND n.deleted = false
          AND (
              (np.userId = :userId AND np.status = 'JOINED' AND np.deleted = false)
              OR 
              (cp.userId = :userId AND cp.status = 'JOINED' AND cp.deleted = false)
          )
    """
    )
    fun existsByNoteIdAndUserAccess(
        @Param("noteId") noteId: Long,
        @Param("userId") userId: Long
    ): Boolean
}
