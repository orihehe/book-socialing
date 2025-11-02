package com.side.book.socialing.presentation.note.dto

import com.side.book.socialing.domain.user.dto.UserDto

data class NoteGuestResponse(
    val user: UserDto,
    val role: String,
    val status: String
)
