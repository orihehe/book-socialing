package com.side.book.socialing.presentation.club.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(name = "CommonClubResponse", description = "일반 클럽 DTO")
data class CommonClubResponse(
    @field:Schema(description = "클럽 ID", example = "1")
    val id: Long,

    @field:Schema(description = "클럽 이름", example = "saisai")
    val clubName: String,

    @field:Schema(
        description = "클럽 이미지 URL 리스트",
        example = "club/1/5750bb25-bb3f-49c7-bf2f-dc8f469a3e08.png",
        format = "uri"
    )
    val clubImageUrls: List<String>,

    @field:Schema(description = "클럽 소개", example = "간단한 클럽 소개")
    val description: String,

    @field:Schema(description = "클럽 멤버 수", example = "8")
    val memberCount: Int
)
