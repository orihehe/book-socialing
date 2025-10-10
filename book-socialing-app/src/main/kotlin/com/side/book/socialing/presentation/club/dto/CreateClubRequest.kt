package com.side.book.socialing.presentation.club.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(name = "CreateClubRequest", description = "클럽 생성 요청")
data class CreateClubRequest(

    @field:Schema(
        description = "클럽 이름",
        example = "스프링 스터디 클럽"
    )
    val clubName: String,

    @field:Schema(
        description = "클럽 소개(선택)",
        example = "스프링/코틀린 북스터디를 진행합니다."
    )
    val description: String?
)
