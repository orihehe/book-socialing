package com.side.book.socialing.global.file

import org.springframework.web.multipart.MultipartFile

// 파일 저장 후의 결과 정보를 담을 데이터 클래스
data class StoredFile(
    val originalFileName: String,
    val storedFileName: String, // 고유하게 저장된 파일명 (ex) a1b2c3d4.jpg)
    val filePath: String // 접근 가능한 전체 경로 (ex) C:/.../a1b2c3d4.jpg)
)

interface FileUploader {
    fun upload(file: MultipartFile, path: String): StoredFile
    fun delete(fullPath: String): Boolean
}
