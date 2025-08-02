package com.side.book.socialing.domain.user.command

data class CreateUserCommand(
    val provider: String,
    val providerId: String,
    val email: String,
    val nickName: String,
    val role: String
)
