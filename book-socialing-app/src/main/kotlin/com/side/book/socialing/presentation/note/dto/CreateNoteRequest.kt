package com.side.book.socialing.presentation.note.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(name = "CreateNoteRequest", description = "노트 생성 요청")
data class CreateNoteRequest(

    @field:Schema(
        description = "클럽 ID (없으면 null)",
        example = "1",
        nullable = true
    )
    val clubId: Long?,

    @field:Schema(
        description = "책 이름",
        example = "스프링 부트의 정석"
    )
    val bookName: String,

    @field:Schema(
        description = "책 저자",
        example = "남궁성"
    )
    val bookAuthor: String,

    @field:Schema(
        description = "노트 소개/설명",
        example = "이것이 자바다 다음으로 볼 책",
        nullable = true
    )
    val description: String?,

    @field:Schema(
        description = "모임일(ISO-8601 LocalDateTime)",
        type = "string",
        format = "date-time",
        example = "2025-08-15T10:00:00"
    )
    val startAt: LocalDateTime,

    @field:Schema(
        description = "탈고일(ISO-8601 LocalDateTime)",
        type = "string",
        format = "date-time",
        example = "2025-09-29T12:00:00"
    )
    val endAt: LocalDateTime
)
