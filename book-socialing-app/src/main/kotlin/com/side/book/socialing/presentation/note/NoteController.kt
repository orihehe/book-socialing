package com.side.book.socialing.presentation.note

import com.side.book.socialing.domain.note.command.CreateNoteCommand
import com.side.book.socialing.domain.note.service.NoteService
import com.side.book.socialing.presentation.note.dto.CommonNoteResponse
import com.side.book.socialing.presentation.note.dto.CreateNoteRequest
import com.side.book.socialing.presentation.note.dto.OpenNoteResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime

@Tag(name = "노트 API", description = "노트 조회, 생성, 참여, 퇴고 등 노트 관련 API")
@RestController
@RequestMapping("/api/note/v1")
class NoteController(
    private val noteService: NoteService
) {
    @PostMapping("/create", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun createNote(
        @RequestPart("request") request: CreateNoteRequest,
        @RequestPart("images") imageFiles: List<MultipartFile>
    ): Long {
        val userId = 123L

        val command =
            CreateNoteCommand(
                userId = userId,
                bookName = request.bookName,
                bookAuthor = request.bookAuthor,
                description = request.description,
                startDate = request.startDate,
                endDate = request.endDate,
                imageFiles = imageFiles
            )

        return noteService.createNote(command).id!!
    }

    @Operation(
        summary = "노트 참여 신청",
        description = "사용자가 특정 노트에 참여를 신청합니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "성공적으로 노트 참여 신청함"),
            ApiResponse(responseCode = "404", description = "해당 노트를 찾을 수 없음")
        ]
    )
    @PostMapping("/{noteId}/join")
    @ResponseStatus(HttpStatus.CREATED)
    fun joinNote(@PathVariable noteId: Long) {
        // TODO: 회원 정보 필요
        val userId = 123L

        noteService.joinNote(userId, noteId)
    }

    @Operation(
        summary = "로그인한 사용자가 참여 중인 열린 노트 목록 조회",
        description = "현재 로그인한 사용자가 참여자로 등록되어 있고, 모임일이 지나지 않은 (즉, 아직 진행될) 노트 목록을 조회합니다. 각 노트의 상세 정보와 참여자 정보가 함께 반환됩니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "성공적으로 열린 노트 목록을 조회함"),
            ApiResponse(responseCode = "500", description = "서버 내부 오류")
        ]
    )
    @GetMapping("/open")
    fun getOpenNotes(): ResponseEntity<List<OpenNoteResponse>> {
        // TODO: 회원 정보 필요
        val userId = 123L

        return try {
            val openNotes = noteService.getOpenNotes(userId)
            ResponseEntity.ok(openNotes)
        } catch (e: Exception) {
            System.err.println("Error fetching open notes for user $userId: ${e.message}")
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(emptyList())
        }
    }

    @Operation(
        summary = "로그인한 사용자가 생성한 노트 목록 조회",
        description = "현재 로그인한 사용자가 직접 생성한 노트 목록을 조회합니다. 각 노트의 기본 정보가 반환됩니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "성공적으로 생성한 노트 목록을 조회함"),
            ApiResponse(responseCode = "500", description = "서버 내부 오류")
        ]
    )
    @GetMapping("/created")
    fun getCreatedNotes(): ResponseEntity<List<CommonNoteResponse>> {
        // TODO: 회원 정보 필요
        val userId = 123L

        return try {
            // val createdNotes = noteService.getCreatedNotes(userId)

            val dummyNotes = mutableListOf<CommonNoteResponse>()

            // 첫 번째 더미 노트
            dummyNotes.add(
                CommonNoteResponse(
                    id = 101L,
                    clubName = "더미 독서모임 A",
                    bookName = "내가 만든 더미 데미안",
                    bookImageUrl = "https://contents.kyobobook.co.kr/sih/fit-in/458x0/pdt/9791164053353.jpg", // 더미 이미지 URL
                    startDateTime = LocalDateTime.of(2025, 8, 1, 19, 0),
                    endDateTime = LocalDateTime.of(2025, 8, 1, 21, 0)
                )
            )

            // 두 번째 더미 노트
            dummyNotes.add(
                CommonNoteResponse(
                    id = 102L,
                    clubName = "더미 독서모임 B",
                    bookName = "내가 만든 더미 어린 왕자",
                    bookImageUrl = "https://contents.kyobobook.co.kr/sih/fit-in/458x0/pdt/9791164455300.jpg", // 또 다른 더미 이미지 URL
                    startDateTime = LocalDateTime.of(2025, 8, 15, 14, 0),
                    endDateTime = LocalDateTime.of(2025, 8, 15, 16, 0)
                )
            )

            ResponseEntity.ok(dummyNotes)
        } catch (e: Exception) {
            System.err.println("Error fetching created notes for user $userId: ${e.message}") // 에러 로깅
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR) // 500 Internal Server Error
                .body(emptyList()) // 빈 리스트 반환 또는 에러 DTO 반환
        }
    }

    @Operation(
        summary = "로그인한 사용자가 신청한 (대기 중인) 노트 목록 조회",
        description = "현재 로그인한 사용자가 참여를 신청했지만, 아직 수락되지 않아 대기 상태인 노트 목록을 조회합니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "성공적으로 신청한 노트 목록을 조회함"),
            ApiResponse(responseCode = "500", description = "서버 내부 오류")
        ]
    )
    @GetMapping("/pending")
    fun getPendingNotes(): ResponseEntity<List<CommonNoteResponse>> {
        // TODO: 회원 정보 필요
        val userId = 123L

        return try {
            // val pendingNotes = noteService.getPendingNotes(userId)

            val dummyNotes = mutableListOf<CommonNoteResponse>()

            // 첫 번째 더미 노트
            dummyNotes.add(
                CommonNoteResponse(
                    id = 101L,
                    clubName = "더미 독서모임 A",
                    bookName = "신청한 더미 데미안",
                    bookImageUrl = "https://contents.kyobobook.co.kr/sih/fit-in/458x0/pdt/9791164053353.jpg", // 더미 이미지 URL
                    startDateTime = LocalDateTime.of(2025, 8, 1, 19, 0),
                    endDateTime = LocalDateTime.of(2025, 8, 1, 21, 0)
                )
            )

            // 두 번째 더미 노트
            dummyNotes.add(
                CommonNoteResponse(
                    id = 102L,
                    clubName = null, // 클럽이 없는 더미 노트
                    bookName = "신청한 더미 어린 왕자",
                    bookImageUrl = "https://contents.kyobobook.co.kr/sih/fit-in/458x0/pdt/9791164455300.jpg", // 또 다른 더미 이미지 URL
                    startDateTime = LocalDateTime.of(2025, 8, 15, 14, 0),
                    endDateTime = LocalDateTime.of(2025, 8, 15, 16, 0)
                )
            )

            ResponseEntity.ok(dummyNotes)
        } catch (e: Exception) {
            System.err.println("Error fetching pending notes for user $userId: ${e.message}") // 에러 로깅
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR) // 500 Internal Server Error
                .body(emptyList()) // 빈 리스트 반환 또는 에러 DTO 반환
        }
    }

    @Operation(
        summary = "로그인한 사용자에게 추천하는 노트 목록 조회",
        description = "로그인한 사용자에게 추천하는 노트 목록을 조회합니다. 각 노트의 기본 정보가 반환됩니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "성공적으로 추천 노트 목록을 조회함"),
            ApiResponse(responseCode = "500", description = "서버 내부 오류")
        ]
    )
    @GetMapping("/recommended")
    fun getRecommendedNotes(): ResponseEntity<List<CommonNoteResponse>> {
        // TODO: 회원 정보 필요
        val userId = 123L

        return try {
            // val recommendedNotes = noteService.getRecommendedNotes(userId)

            val dummyNotes = mutableListOf<CommonNoteResponse>()

            // 첫 번째 더미 노트
            dummyNotes.add(
                CommonNoteResponse(
                    id = 101L,
                    clubName = "더미 독서모임 A",
                    bookName = "추천 더미 데미안",
                    bookImageUrl = "https://contents.kyobobook.co.kr/sih/fit-in/458x0/pdt/9791164053353.jpg", // 더미 이미지 URL
                    startDateTime = LocalDateTime.of(2025, 8, 1, 19, 0),
                    endDateTime = LocalDateTime.of(2025, 8, 1, 21, 0)
                )
            )

            // 두 번째 더미 노트
            dummyNotes.add(
                CommonNoteResponse(
                    id = 102L,
                    clubName = null, // 클럽이 없는 더미 노트
                    bookName = "추천 더미 어린 왕자",
                    bookImageUrl = "https://contents.kyobobook.co.kr/sih/fit-in/458x0/pdt/9791164455300.jpg", // 또 다른 더미 이미지 URL
                    startDateTime = LocalDateTime.of(2025, 8, 15, 14, 0),
                    endDateTime = LocalDateTime.of(2025, 8, 15, 16, 0)
                )
            )

            ResponseEntity.ok(dummyNotes)
        } catch (e: Exception) {
            System.err.println("Error fetching recommended notes for user $userId: ${e.message}") // 에러 로깅
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR) // 500 Internal Server Error
                .body(emptyList()) // 빈 리스트 반환 또는 에러 DTO 반환
        }
    }

    @Operation(
        summary = "로그인한 사용자의 퇴고한 노트 목록 조회",
        description = "현재 로그인한 사용자가 참여했으며, 모임일이 지난 노트 목록을 조회합니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "성공적으로 퇴고한 노트 목록을 조회함"),
            ApiResponse(responseCode = "500", description = "서버 내부 오류")
        ]
    )
    @GetMapping("/revised")
    fun getRevisedNotes(): ResponseEntity<List<CommonNoteResponse>> {
        // TODO: 회원 정보 필요
        val userId = 123L

        return try {
            // val revisedNotes = noteService.getParticipatedRevisedNotes(userId)

            val dummyNotes = mutableListOf<CommonNoteResponse>()

            // 첫 번째 더미 노트
            dummyNotes.add(
                CommonNoteResponse(
                    id = 101L,
                    clubName = "더미 독서모임 A",
                    bookName = "퇴고한 더미 데미안",
                    bookImageUrl = "https://contents.kyobobook.co.kr/sih/fit-in/458x0/pdt/9791164053353.jpg", // 더미 이미지 URL
                    startDateTime = LocalDateTime.of(2025, 8, 1, 19, 0),
                    endDateTime = LocalDateTime.of(2025, 8, 1, 21, 0)
                )
            )

            // 두 번째 더미 노트
            dummyNotes.add(
                CommonNoteResponse(
                    id = 102L,
                    clubName = null, // 클럽이 없는 더미 노트
                    bookName = "퇴고한 더미 어린 왕자",
                    bookImageUrl = "https://contents.kyobobook.co.kr/sih/fit-in/458x0/pdt/9791164455300.jpg", // 또 다른 더미 이미지 URL
                    startDateTime = LocalDateTime.of(2025, 8, 15, 14, 0),
                    endDateTime = LocalDateTime.of(2025, 8, 15, 16, 0)
                )
            )

            ResponseEntity.ok(dummyNotes)
        } catch (e: Exception) {
            System.err.println("Error fetching revised notes for user $userId: ${e.message}") // 에러 로깅
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR) // 500 Internal Server Error
                .body(emptyList()) // 빈 리스트 반환 또는 에러 DTO 반환
        }
    }
}
