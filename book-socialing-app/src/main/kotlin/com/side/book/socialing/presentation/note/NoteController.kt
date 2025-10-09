package com.side.book.socialing.presentation.note

import com.side.book.socialing.domain.note.command.CreateNoteCommand
import com.side.book.socialing.domain.note.service.NoteService
import com.side.book.socialing.global.auth.UserPrincipalResolver
import com.side.book.socialing.presentation.note.dto.ClubNotesPageResponse
import com.side.book.socialing.presentation.note.dto.CommonNoteResponse
import com.side.book.socialing.presentation.note.dto.CreateNoteRequest
import com.side.book.socialing.presentation.note.dto.GetNoteResponse
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

@Tag(name = "노트 API", description = "노트 조회, 생성, 참여, 퇴고 등 노트 관련 API")
@RestController
@RequestMapping("/api/note/v1")
class NoteController(
    private val noteService: NoteService,
    private val userPrincipalResolver: UserPrincipalResolver
) {
    @PostMapping("/create", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    @Operation(
        summary = "노트 생성",
        description = "JSON(요청 본문) + 이미지 파일 업로드"
    )
    fun createNote(
        @RequestPart("request") request: CreateNoteRequest,
        @RequestPart("images") imageFiles: List<MultipartFile>
    ): Long {
        val userId = userPrincipalResolver.getUserId()

        val command =
            CreateNoteCommand(
                userId = userId,
                clubId = request.clubId,
                bookName = request.bookName,
                bookAuthor = request.bookAuthor,
                description = request.description,
                startAt = request.startAt,
                endAt = request.endAt,
                imageFiles = imageFiles
            )

        return noteService.createNote(command)
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
    fun getOpenNotes(
        @RequestParam(defaultValue = "10") pageSize: Int,
        @RequestParam(defaultValue = "1") pageNum: Int
    ): ResponseEntity<ClubNotesPageResponse<OpenNoteResponse>> {
        val userId = userPrincipalResolver.getUserId()
        val offset = (pageNum - 1) * pageSize

        return try {
            val openNotes = noteService.getOpenNotes(userId, pageSize, offset)
            ResponseEntity.ok(openNotes)
        } catch (e: Exception) {
            System.err.println("Error fetching open notes for user $userId: ${e.message}")
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ClubNotesPageResponse(totalCount = 0L, groups = emptyList()))
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
    fun getCreatedNotes(
        @RequestParam(defaultValue = "10") pageSize: Int,
        @RequestParam(defaultValue = "1") pageNum: Int
    ): ResponseEntity<ClubNotesPageResponse<CommonNoteResponse>> {
        val userId = userPrincipalResolver.getUserId()
        val offset = (pageNum - 1) * pageSize

        return try {
            val createdNotes = noteService.getCreatedNotes(userId, pageSize, offset)
            ResponseEntity.ok(createdNotes)
        } catch (e: Exception) {
            System.err.println("Error fetching created notes for user $userId: ${e.message}") // 에러 로깅
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ClubNotesPageResponse(totalCount = 0L, groups = emptyList()))
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
    fun getPendingNotes(
        @RequestParam(defaultValue = "10") pageSize: Int,
        @RequestParam(defaultValue = "1") pageNum: Int
    ): ResponseEntity<ClubNotesPageResponse<CommonNoteResponse>> {
        val userId = userPrincipalResolver.getUserId()
        val offset = (pageNum - 1) * pageSize

        return try {
            val pendingNotes = noteService.getPendingNotes(userId, pageSize, offset)
            ResponseEntity.ok(pendingNotes)
        } catch (e: Exception) {
            System.err.println("Error fetching pending notes for user $userId: ${e.message}") // 에러 로깅
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ClubNotesPageResponse(totalCount = 0L, groups = emptyList()))
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
    @GetMapping("/recommend")
    fun getRecommendNotes(): ResponseEntity<List<CommonNoteResponse>> {
        val userId = userPrincipalResolver.getUserId()

        return try {
            val recommendNotes = noteService.getRecommendNotes(userId)
            ResponseEntity.ok(recommendNotes)
        } catch (e: Exception) {
            System.err.println("Error fetching recommend notes for user $userId: ${e.message}") // 에러 로깅
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(emptyList())
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
    fun getRevisedNotes(
        @RequestParam(defaultValue = "10") pageSize: Int,
        @RequestParam(defaultValue = "1") pageNum: Int
    ): ResponseEntity<ClubNotesPageResponse<CommonNoteResponse>> {
        val userId = userPrincipalResolver.getUserId()
        val offset = (pageNum - 1) * pageSize

        return try {
            val revisedNotes = noteService.getRevisedNotes(userId, pageSize, offset)
            ResponseEntity.ok(revisedNotes)
        } catch (e: Exception) {
            System.err.println("Error fetching revised notes for user $userId: ${e.message}") // 에러 로깅
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ClubNotesPageResponse(totalCount = 0L, groups = emptyList()))
        }
    }

    @GetMapping("/{noteId}")
    @Operation(
        summary = "특정 노트 조회",
        description = "노트 ID를 통해 특정 노트의 상세 정보를 조회합니다."
    )
    @ResponseStatus(HttpStatus.OK) // 200 OK 반환
    fun getNoteById(
        @PathVariable noteId: Long // URL 경로에서 noteId를 받음
    ): ResponseEntity<GetNoteResponse> {
        val userId = userPrincipalResolver.getUserId()

        return try {
            val note = noteService.getNoteById(noteId, userId)
            return ResponseEntity.ok(note)
        } catch (e: Exception) {
            System.err.println("Error fetching revised notes for user $userId: ${e.message}") // 에러 로깅
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }
}
