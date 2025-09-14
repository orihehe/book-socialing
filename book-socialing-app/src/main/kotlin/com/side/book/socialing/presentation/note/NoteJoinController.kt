package com.side.book.socialing.presentation.note

import com.side.book.socialing.domain.note.service.NoteJoinService
import com.side.book.socialing.global.auth.UserPrincipalResolver
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@Tag(name = "노트 참여 API", description = "노트 참여 관련 API")
@RestController
@RequestMapping("/api/note/v1")
class NoteJoinController(
    private val noteJoinService: NoteJoinService,
    private val userPrincipalResolver: UserPrincipalResolver
) {

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
    @PostMapping("/{noteId}/join/request")
    @ResponseStatus(HttpStatus.CREATED)
    fun joinRequest(@PathVariable noteId: Long) {
        val userId = userPrincipalResolver.getUserId()
        noteJoinService.joinRequest(userId, noteId)
    }

    @Operation(
        summary = "노트 참여 신청 취소",
        description = "사용자가 특정 노트 참여 신청한 것을 취소합니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "성공적으로 노트 참여 취소함"),
            ApiResponse(responseCode = "404", description = "해당 노트를 찾을 수 없음")
        ]
    )
    @PatchMapping("/{noteId}/join/cancel")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun cancelRequest(@PathVariable noteId: Long) {
        val userId = userPrincipalResolver.getUserId()
        noteJoinService.cancelRequest(userId, noteId)
    }

    @Operation(
        summary = "노트 참여 신청 승인",
        description = "관리자가 노트 참여 신청을 승인합니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "성공적으로 노트 참여 신청을 승인함"),
            ApiResponse(responseCode = "404", description = "해당 노트 또는 참가자를 찾을 수 없음")
        ]
    )
    @PatchMapping("/{noteId}/participants/{approvedUserId}/approve")
    @ResponseStatus(HttpStatus.OK)
    fun approve(@PathVariable noteId: Long, @PathVariable approvedUserId: Long) {
        val userId = userPrincipalResolver.getUserId()
        noteJoinService.approve(userId, noteId, approvedUserId)
    }

    @Operation(
        summary = "노트 참여 신청 거절",
        description = "관리자가 노트 참여 신청을 거절합니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "성공적으로 노트 참여 신청을 거절함"),
            ApiResponse(responseCode = "404", description = "해당 노트 또는 참가자를 찾을 수 없음")
        ]
    )
    @PatchMapping("/{noteId}/participants/{rejectedUserId}/reject")
    @ResponseStatus(HttpStatus.OK)
    fun reject(@PathVariable noteId: Long, @PathVariable rejectedUserId: Long) {
        val userId = userPrincipalResolver.getUserId()
        noteJoinService.reject(userId, noteId, rejectedUserId)
    }

    @Operation(
        summary = "노트 참여자 강퇴",
        description = "관리자가 노트 참여자를 강퇴합니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "성공적으로 노트 참여자를 강퇴함"),
            ApiResponse(responseCode = "404", description = "해당 노트 또는 참가자를 찾을 수 없음")
        ]
    )
    @PatchMapping("/{noteId}/participants/{kickedUserId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun kick(@PathVariable noteId: Long, @PathVariable kickedUserId: Long) {
        val userId = userPrincipalResolver.getUserId()
        noteJoinService.kick(userId, noteId, kickedUserId)
    }
}
