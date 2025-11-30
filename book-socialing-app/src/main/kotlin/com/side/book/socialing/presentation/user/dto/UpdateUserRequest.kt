package com.side.book.socialing.presentation.user.dto

data class UpdateUserRequest(
    val userId: Long,
    val nickname: String,
    val description: String
)
