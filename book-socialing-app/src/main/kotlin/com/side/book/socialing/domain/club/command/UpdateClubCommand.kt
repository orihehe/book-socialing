package com.side.book.socialing.domain.club.command

import com.side.book.socialing.global.file.FileValidator
import org.springframework.web.multipart.MultipartFile

data class UpdateClubCommand(
    val clubId: Long,
    val userId: Long,
    val clubName: String,
    val description: String?,
    val imageFiles: List<MultipartFile>,
    val deletedFileIds: List<Long> = emptyList()
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
