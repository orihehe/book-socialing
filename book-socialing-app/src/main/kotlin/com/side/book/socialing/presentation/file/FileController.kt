package com.side.book.socialing.presentation.file

import com.side.book.socialing.global.file.FileUploader
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.core.io.Resource
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Tag(name = "파일 API", description = "파일 관련 API")
@CrossOrigin(origins = ["http://localhost:3000"])
@RestController
@RequestMapping("/api/v1/file")
class FileController(private val fileUploader: FileUploader) {

    @Operation(
            summary = "파일 조회 (이미지 또는 일반 파일)",
            description = " 요청한 파일 경로(`filePath`)에 해당하는 파일을 반환합니다."
    )
    @ApiResponses(
            value =
                    [
                            ApiResponse(responseCode = "200", description = "요청한 파일을 성공적으로 반환함"),
                            ApiResponse(
                                    responseCode = "400",
                                    description = "잘못된 요청 파라미터 (예: filePath 누락 또는 형식 오류)"
                            ),
                            ApiResponse(responseCode = "500", description = "서버 내부 오류")]
    )
    @GetMapping
    fun viewFile(@RequestParam filePath: String): ResponseEntity<Resource> {
        val resource = fileUploader.getFileAsResource(filePath)
        val file = fileUploader.getFile(filePath)
        val contentType = determineContentType(file.extension)

        return ResponseEntity.ok().contentType(contentType).body(resource)
    }

    private fun determineContentType(extension: String): MediaType {
        return when (extension) {
            "jpg", "jpeg" -> MediaType.IMAGE_JPEG
            "png" -> MediaType.IMAGE_PNG
            "gif" -> MediaType.IMAGE_GIF
            "pdf" -> MediaType.APPLICATION_PDF
            else -> MediaType.APPLICATION_OCTET_STREAM
        }
    }
}
