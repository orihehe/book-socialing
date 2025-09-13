package com.side.book.socialing.domain.club.command

import org.springframework.web.multipart.MultipartFile

data class CreateClubCommand(
    val userId: Long,
    val clubName: String,
    val description: String?,
    val imageFiles: List<MultipartFile> // 업로드된 파일 목록
)
