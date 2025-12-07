package com.side.book.socialing.domain.user.dto

import com.side.book.socialing.domain.user.entity.User

data class UserDto(
    val id: Long,
    val email: String?,
    val nickname: String,
    val profileImageUrl: String?
) {
    companion object {
        fun from(user: User): UserDto {
            return UserDto(
                id = user.id!!,
                email = user.email,
                nickname = user.nickname,
                profileImageUrl = user.profileImageUrl
            )
        }
    }
}
