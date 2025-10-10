package com.side.book.socialing.domain.note.command

import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime

data class UpdateNoteCommand(
    val noteId: Long,
    val userId: Long,
    val bookName: String,
    val bookAuthor: String,
    val description: String,
    val startAt: LocalDateTime,
    val endAt: LocalDateTime,
    val imageFiles: List<MultipartFile>
)
