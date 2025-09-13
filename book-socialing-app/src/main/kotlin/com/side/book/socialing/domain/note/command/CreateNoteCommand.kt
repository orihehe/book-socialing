package com.side.book.socialing.domain.note.command

import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime

data class CreateNoteCommand(
    val userId: Long,
    val clubId: Long?,
    val bookName: String,
    val bookAuthor: String,
    val description: String?,
    val startDate: LocalDateTime,
    val endDate: LocalDateTime,
    val imageFiles: List<MultipartFile> // 업로드된 파일 목록
)
