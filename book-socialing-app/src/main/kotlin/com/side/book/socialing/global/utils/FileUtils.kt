package com.side.book.socialing.global.utils

import java.util.UUID

class FileUtils {
    companion object {
        /**
         * 저장할 파일의 고유한 이름을 생성합니다.
         * @param originalFilename 원본 파일 이름 (예: "내사진.jpg")
         * @return 고유한 파일 이름 (예: "a1b2c3d4-e5f6-g7h8-i9j0.jpg")
         */
        fun createStoredFileName(originalFilename: String): String {
            val fileExtension = extractExtension(originalFilename)
            val uuid = UUID.randomUUID().toString()

            return "$uuid.$fileExtension"
        }

        /**
         * 파일 이름에서 확장자를 추출합니다.
         * @param originalFilename 원본 파일 이름
         * @return 확장자 (예: "jpg"). 확장자가 없으면 빈 문자열을 반환합니다.
         */
        fun extractExtension(originalFilename: String): String {
            val dotPosition = originalFilename.lastIndexOf('.')

            return if (dotPosition != -1 && dotPosition < originalFilename.length - 1) {
                originalFilename.substring(dotPosition + 1)
            } else {
                ""
            }
        }
    }
}
