package com.side.book.socialing.global.file

import com.side.book.socialing.global.utils.FileUtils
import com.side.book.socialing.global.utils.log
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import java.io.IOException

import java.nio.file.*

@Component
class LocalFileUploader(
    @Value("\${file.base-dir}") private val baseDir: String
): FileUploader {

    /**
     * 전달된 파일을 서버의 지정된 경로에 업로드하고 저장 정보를 반환합니다.
     *
     * @param file 업로드할 파일 (MultipartFile)
     * @param path 저장할 하위 경로 (예: "notes/images")
     * @return 저장된 파일의 정보 (원본명, 저장명, 전체 경로)가 담긴 `StoredFile` 객체.
     *         (예: StoredFile(originalFileName="내사진.jpg", storedFileName="a1b2c3d4.jpg", filePath="C:/.../a1b2c3d4.jpg"))
     */
    override fun upload(file: MultipartFile, path: String): StoredFile {
        val uploadFileDir = Paths.get(baseDir, path)

        if (!Files.exists(uploadFileDir)) {
            Files.createDirectories(uploadFileDir)
        }

        val originalFileName = file.originalFilename ?: "unnamed"
        val storedFileName = FileUtils.createStoredFileName(originalFileName)
        val fullPath: Path = uploadFileDir.resolve(storedFileName)

        file.transferTo(fullPath.toFile())

        return StoredFile(
            originalFileName = originalFileName,
            storedFileName = storedFileName,
            filePath = fullPath.toString()
        )
    }

    /**
     * 지정된 전체 경로의 로컬 파일을 삭제합니다.
     * @param fullPath 삭제할 파일의 전체 경로 (예: "C:/uploads/notes/images/a1b2c3d4.jpg")
     * @return 삭제에 성공하면 true, 파일이 없거나 다른 이유로 실패하면 false를 반환합니다.
     */
    override fun delete(fullPath: String): Boolean {
        return try {
            val filePath: Path = Paths.get(fullPath)

            if (Files.exists(filePath)) {
                Files.delete(filePath)
                log.info("[SUCCESS] 파일 삭제 성공: $fullPath")
                true
            } else {

                log.error("[WARN] 삭제할 파일이 존재하지 않습니다: $fullPath")
                true
            }

        } catch (e: IOException) {
            log.error("[ERROR] 파일 삭제 중 오류 발생: $fullPath")
            e.printStackTrace()
            false
        } catch (e: SecurityException) {
            log.error("[ERROR] 파일 삭제에 필요한 권한이 없습니다: $fullPath")
            e.printStackTrace()
            false
        }
    }
}
