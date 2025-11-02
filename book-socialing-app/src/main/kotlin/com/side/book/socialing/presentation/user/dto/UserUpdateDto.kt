package com.side.book.socialing.presentation.user.dto

import org.springframework.web.multipart.MultipartFile

data class UserUpdateDto(
    val nickname: String,
    val profileImage: MultipartFile?,
    val description: String?
) {
    init {
        val trimmedNickname = nickname.trim()
        require(trimmedNickname.length <= 20) { "닉네임은 최대 20자까지 가능합니다." }
        require(trimmedNickname.length >= 2) { "닉네임은 최소 2자부터 가능합니다." }
    }
}
