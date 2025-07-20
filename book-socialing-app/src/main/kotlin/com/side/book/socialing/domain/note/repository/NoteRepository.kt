package com.side.book.socialing.domain.note.repository

import com.side.book.socialing.domain.note.entity.Note
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface NoteRepository: JpaRepository<Note, Long> {
    fun findByCreatedByAndEndDateAfter(createdBy: String, endDate: LocalDateTime): List<Note>
}

