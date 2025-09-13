package com.side.book.socialing.domain.club.command

import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.web.multipart.MultipartFile

@Schema(
    name = "CreateClubCommand",
    description = "클럽 생성 커맨드(서버 내부용). API 문서는 일반적으로 CreateClubRequest를 노출합니다."
)
data class CreateClubCommand(

    @field:Schema(description = "요청 사용자 ID", example = "1")
    val userId: Long,

    @field:Schema(description = "클럽 이름", example = "클럽1")
    val clubName: String,

    @field:Schema(description = "클럽 소개", example = "스프링/자바 스터디 클럽")
    val description: String?,

    @field:ArraySchema(schema = Schema(type = "string", format = "binary"))
    @field:Schema(description = "클럽 이미지 파일 목록(다중 업로드)")
    val imageFiles: List<MultipartFile>
)
