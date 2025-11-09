package com.side.book.socialing.presentation.user.dto

import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "사용자 정보")
data class UserResponse(
    @field:Schema(description = "사용자 ID", example = "1")
    val id: Long?,

    @field:Schema(description = "이메일", example = "saisai@saisai.com")
    val email: String,

    @field:Schema(description = "닉네임", example = "saisai")
    val nickname: String,

    @field:Schema(description = "사용자 소개", example = "나는야 개발자", nullable = true)
    val description: String?,

    @field:Schema(description = "사용자 역할", example = "ROLE_USER")
    val role: String,

    @field:Schema(description = "대표 이미지 URL", format = "uri", example = "/note/27/cover1.png")
    val imageUrl: String?
)
