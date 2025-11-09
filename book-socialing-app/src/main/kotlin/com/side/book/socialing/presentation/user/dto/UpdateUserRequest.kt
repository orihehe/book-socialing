package com.side.book.socialing.presentation.user.dto

import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime

data class UpdateUserRequest(
    val userId: Long,
    val nickname: String,
    val description: String
)
