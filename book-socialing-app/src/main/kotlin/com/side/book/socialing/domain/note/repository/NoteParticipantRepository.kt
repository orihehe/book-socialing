package com.side.book.socialing.domain.note.repository

import com.side.book.socialing.domain.note.entity.NoteParticipant
import com.side.book.socialing.domain.note.enum.ParticipantStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface NoteParticipantRepository : JpaRepository<NoteParticipant, Long> {
    fun findByNoteIdAndUserId(noteId: Long, userId: Long): NoteParticipant?
    fun findAllByNoteIdAndStatusIn(noteId: Long, statuses: Set<ParticipantStatus>): List<NoteParticipant>
    fun findAllByUserId(userId: Long): List<NoteParticipant>

    @Query(
        """
        SELECT DISTINCT 
            CASE 
                WHEN np.userId IS NOT NULL THEN np.userId 
                ELSE cp.userId 
            END
        FROM Note n
        LEFT JOIN n.participants np ON np.note = n AND np.status = 'JOINED' AND np.deleted = false
        LEFT JOIN n.club c
        LEFT JOIN c.participants cp ON cp.club = c AND cp.status = 'JOINED' AND cp.deleted = false
        WHERE n.id = :noteId
          AND n.deleted = false
          AND (np.userId IS NOT NULL OR cp.userId IS NOT NULL)
    """
    )
    fun findAllJoinedUserIdsByNoteId(@Param("noteId") noteId: Long): Set<Long>
}
