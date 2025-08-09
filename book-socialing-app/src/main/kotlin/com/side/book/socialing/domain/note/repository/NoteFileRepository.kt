package com.side.book.socialing.domain.note.repository

import com.side.book.socialing.domain.note.entity.NoteFile
import org.springframework.data.jpa.repository.JpaRepository


interface NoteFileRepository: JpaRepository<NoteFile, Long> {

}
