package com.side.book.socialing.domain.note.command

import com.side.book.socialing.global.file.FileValidator
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
) {
    init {
        if (imageFiles.size !in 1..3) {
            throw IllegalArgumentException("Invalid number of image files. User must upload between 1 and 3 images.")
        }

        imageFiles.forEach {
            FileValidator.validateImageFile(it)
        }
    }
}
