package com.side.book.socialing.presentation.note.dto

import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(description = "클럽별 노트 페이지 응답(제네릭)")
data class ClubNotesPageResponse<T>(
    @field:Schema(description = "전체 노트 수(페이징/그룹 전)", example = "26")
    val totalCount: Long,

    // 제네릭(T) 특성상 구체 타입은 컨트롤러 반환 타입에서 결정됨
    @field:Schema(description = "클럽별 그룹 리스트")
    val groups: List<ClubNotesGroupResponse<T>>
)

@Schema(description = "클럽 단위 그룹(제네릭)")
data class ClubNotesGroupResponse<T>(
    @field:Schema(description = "클럽 ID. 클럽이 없으면 null", example = "1", nullable = true)
    val clubId: Long?,

    @field:Schema(description = "클럽명. 클럽이 없으면 'NONE'", example = "saisai")
    val clubName: String,

    @field:Schema(description = "클럽에 속한 노트 리스트")
    val notes: List<T>
)

@Schema(description = "일반 노트 DTO")
data class CommonNoteResponse(
    @field:Schema(description = "노트 ID", example = "1")
    val id: Long,

    @field:Schema(description = "클럽 이름", example = "saisai", nullable = true)
    val clubName: String?,

    @field:Schema(description = "책 이름", example = "두 개의 탑")
    val bookName: String,

    @field:Schema(description = "책 이미지 URL", example = "https://covers.openlibrary.org/b/id/8231856-L.jpg", format = "uri")
    val bookImageUrl: String,

    @field:Schema(description = "모임일(ISO-8601)", type = "string", format = "date-time", example = "2025-06-22T10:00:00")
    val startAt: LocalDateTime,

    @field:Schema(description = "탈고일(ISO-8601)", type = "string", format = "date-time", example = "2025-06-28T12:00:00")
    val endAt: LocalDateTime
)

@Schema(description = "작성중인 노트 DTO")
data class OpenNoteResponse(
    @field:Schema(description = "노트 ID", example = "1")
    val id: Long,

    @field:Schema(description = "책 이름", example = "두 개의 탑")
    val bookName: String,

    @field:Schema(description = "책 저자", example = "JRR Tolkien")
    val bookAuthor: String,

    @field:Schema(description = "책 이미지 URL", example = "https://covers.openlibrary.org/b/id/8231856-L.jpg", format = "uri")
    val bookImageUrl: String,

    @field:Schema(description = "노트 소개", example = "~클럽의 몇번째 책임입니다", nullable = true)
    val description: String?,

    @field:ArraySchema(schema = Schema(implementation = ParticipantInfoResponse::class))
    @field:Schema(description = "노트 참여 멤버 목록")
    val participants: List<ParticipantInfoResponse>,

    @field:Schema(description = "모임일(ISO-8601)", type = "string", format = "date-time", example = "2025-06-22T10:00:00")
    val startAt: LocalDateTime,

    @field:Schema(description = "탈고일(ISO-8601)", type = "string", format = "date-time", example = "2025-06-28T12:00:00")
    val endAt: LocalDateTime
)

@Schema(description = "개별 노트 조회 DTO")
data class GetNoteResponse(
    @field:Schema(description = "노트 ID", example = "27")
    val id: Long,

    @field:Schema(description = "클럽 ID", example = "1", nullable = true)
    val clubId: Long?,

    @field:Schema(description = "클럽명", example = "클럽1", nullable = true)
    val clubName: String?,

    @field:Schema(description = "책 제목", example = "스프링 부트의 정석")
    val bookName: String,

    @field:Schema(description = "책 저자", example = "남궁성")
    val bookAuthor: String,

    @field:Schema(description = "노트 소개", example = "이것이 자바다 다음으로 볼 책", nullable = true)
    val description: String?,

    @field:ArraySchema(arraySchema = Schema(description = "이미지 URL 목록"), schema = Schema(format = "uri", example = "/note/27/cover1.png"))
    val imageUrls: List<String>,

    @field:ArraySchema(schema = Schema(implementation = ParticipantInfoResponse::class))
    @field:Schema(description = "참여자 목록")
    val participants: List<ParticipantInfoResponse>,

    @field:Schema(description = "모임일", type = "string", format = "date-time", example = "2025-08-15T10:00:00")
    val startAt: LocalDateTime,

    @field:Schema(description = "탈고일", type = "string", format = "date-time", example = "2025-09-29T12:00:00")
    val endAt: LocalDateTime
)
