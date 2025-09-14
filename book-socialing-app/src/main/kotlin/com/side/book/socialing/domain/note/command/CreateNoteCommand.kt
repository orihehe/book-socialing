package com.side.book.socialing.domain.note.command

import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime

@Schema(
    name = "CreateNoteCommand",
    description = "노트 생성 커맨드(서버 내부용). API 요청 바디 문서는 CreateNoteRequest 참고."
)
data class CreateNoteCommand(

    @field:Schema(description = "요청 사용자 ID", example = "1")
    val userId: Long,

    @field:Schema(description = "클럽 ID(없으면 null)", example = "1", nullable = true)
    val clubId: Long?,

    @field:Schema(description = "책 이름", example = "스프링 부트의 정석")
    val bookName: String,

    @field:Schema(description = "책 저자", example = "남궁성")
    val bookAuthor: String,

    @field:Schema(description = "노트 소개/설명", example = "이것이 자바다 다음으로 볼 책")
    val description: String?,

    @field:Schema(
        description = "모임일(ISO-8601)",
        type = "string", format = "date-time",
        example = "2025-08-15T10:00:00"
    )
    val startAt: LocalDateTime,

    @field:Schema(
        description = "탈고일(ISO-8601)",
        type = "string", format = "date-time",
        example = "2025-09-29T12:00:00"
    )
    val endAt: LocalDateTime,

    @field:ArraySchema(schema = Schema(type = "string", format = "binary"))
    @field:Schema(description = "업로드 이미지 파일 목록")
    val imageFiles: List<MultipartFile>
)
