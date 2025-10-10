package com.side.book.socialing.global.file

import org.springframework.web.multipart.MultipartFile

object FileValidator {
    private val ALLOWED_IMAGE_EXTENSIONS = setOf("jpg", "jpeg", "png")
    private val ALLOWED_IMAGE_CONTENT_TYPES = setOf("image/jpeg", "image/png")
    private const val MAX_IMAGE_SIZE = 5 * 1024 * 1024L // TODO: 확인 후 재정의 필요

    fun validateImageFile(file: MultipartFile) {
        validateExtension(file)
        validateContentType(file)
        validateSize(file)
    }

    private fun validateExtension(file: MultipartFile) {
        val extension = file.originalFilename
            ?.substringAfterLast('.', "")
            ?.lowercase()
            ?: throw IllegalArgumentException("Can't find file extension")

        if (extension !in ALLOWED_IMAGE_EXTENSIONS) {
            throw IllegalArgumentException(
                "File extension $extension doesn't allowed"
            )
        }
    }

    private fun validateContentType(file: MultipartFile) {
        val contentType = file.contentType?.lowercase()
        if (contentType !in ALLOWED_IMAGE_CONTENT_TYPES) {
            throw IllegalArgumentException(
                "Invalid file type. Only image files are allowed."
            )
        }
    }

    private fun validateSize(file: MultipartFile) {
        if (file.size > MAX_IMAGE_SIZE) {
            throw IllegalArgumentException(
                "File size is too large. The maximum allowed size is 5MB."
            )
        }
    }
}
