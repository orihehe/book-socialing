package com.side.book.socialing.domain.user.command

import org.springframework.web.multipart.MultipartFile

data class UpdateUserCommand(
    val userId: Long,
    val nickname: String,
    val description: String?,
    val imageFile: MultipartFile?
)
