package com.side.book.socialing.presentation.note.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(name = "ParticipantInfoResponse", description = "노트 참여자 정보 DTO")
data class ParticipantInfoResponse(
    @field:Schema(description = "노트 참여자 ID", example = "28")
    val participantId: Long,

    @field:Schema(description = "사용자 ID", example = "1")
    val userId: Long,

    @field:Schema(
        description = "노트 참여자 역할",
        example = "HOST",
        allowableValues = ["HOST", "MEMBER"]
    )
    val role: String,

    @field:Schema(
        description = "노트 참여자 상태",
        example = "JOINED",
        allowableValues = ["JOINED", "PENDING_APPROVAL", "CANCEL", "REJECTED", "LEFT", "KICKED"]
    )
    val status: String
)

