package com.side.book.socialing.domain.club.repository

import com.side.book.socialing.domain.note.entity.NoteFile
import org.springframework.data.jpa.repository.JpaRepository

interface ClubFileRepository: JpaRepository<NoteFile, Long> {

}
