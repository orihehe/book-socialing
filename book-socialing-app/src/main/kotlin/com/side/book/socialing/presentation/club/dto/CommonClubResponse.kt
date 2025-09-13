package com.side.book.socialing.presentation.club.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "일반 클럽 DTO")
data class CommonClubResponse(
    @Schema(description = "클럽 ID", example = "1")
    val id: Long,
    @Schema(description = "클럽 이름", example = "saisai")
    val clubName: String,
    @Schema(description = "클럽 이미지 URL", example = "https://dummyimage.com/600x400/000/fff&text=DummyClub")
    val clubImageUrl: String,
    @Schema(description = "클럽 소개", example = "간단한 클럽 소개")
    val description: String,
    @Schema(description = "클럽 멤버 수", example = "8")
    val memberCount: Int
)
