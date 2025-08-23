package com.side.book.socialing.presentation.note.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "노트 참여자 정보 DTO")
data class ParticipantInfoResponse(
    @Schema(description = "노트 참여자 id", example = "1")
    val participantId: Long,
    @Schema(description = "노트 참여자 user id", example = "1")
    val userId: Long,
    @Schema(description = "노트 참여자 역할", example = "HOST")
    val role: String,
    @Schema(description = "노트 참여자 상태", example = "JOINED")
    val status: String
)
