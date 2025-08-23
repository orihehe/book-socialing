package com.side.book.socialing.domain.note.repository

import com.side.book.socialing.domain.note.entity.Note
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query


interface NoteRepository: JpaRepository<Note, Long> {

    @Query("SELECT n FROM Note n JOIN n.participants p WHERE p.userId = :userId AND p.status = 'JOINED'")
    fun findActiveNotesByUserId(userId: Long): List<Note>

}
