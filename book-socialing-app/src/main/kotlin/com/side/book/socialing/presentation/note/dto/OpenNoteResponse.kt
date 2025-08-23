package com.side.book.socialing.presentation.note.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(description = "작성중인 노트 DTO")
data class OpenNoteResponse(
    @Schema(description = "노트 ID", example = "1")
    val id: Long,
    @Schema(description = "클럽 이름", example = "saisai")
    val clubName: String?,
    @Schema(description = "책 이름", example = "두 개의 탑")
    val bookName: String,
    @Schema(description = "책 작가", example = "JRR Tolkein")
    val bookAuthor: String,
    @Schema(description = "책 이미지 URL", example = "https://covers.openlibrary.org/b/id/8231856-L.jpg")
    val bookImageUrl: String,
    @Schema(description = "노트 소개", example = "~클럽의 몇번째 책임입니다")
    val description: String?,
    @Schema(description = "노트 참여 멤버 목록")
    val participantList: List<ParticipantInfoResponse>,
    @Schema(description = "모임일", example = "2025-06-22")
    val startDateTime: LocalDateTime,
    @Schema(description = "탈고일", example = "2025-06-28")
    val endDateTime: LocalDateTime,
)
