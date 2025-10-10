package com.side.book.socialing.presentation.club

import com.side.book.socialing.domain.club.service.ClubJoinService
import com.side.book.socialing.global.auth.UserPrincipalResolver
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@Tag(name = "클럽 참여 API", description = "클럽 참여 관련 API")
@RestController
@RequestMapping("/api/v1/club")
class ClubMemberController(
    private val clubJoinService: ClubJoinService,
    private val userPrincipalResolver: UserPrincipalResolver
) {

    @Operation(
        summary = "클럽 참여 신청",
        description = "사용자가 특정 클럽에 참여를 신청합니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "성공적으로 클럽 참여 신청함"),
            ApiResponse(responseCode = "404", description = "해당 클럽을 찾을 수 없음")
        ]
    )
    @PostMapping("/{clubId}/join/request")
    @ResponseStatus(HttpStatus.CREATED)
    fun joinRequest(@PathVariable clubId: Long) {
        val userId = userPrincipalResolver.getUserId()
        clubJoinService.joinRequest(userId, clubId)
    }

    @Operation(
        summary = "클럽 참여 신청 취소",
        description = "사용자가 특정 클럽 참여 신청한 것을 취소합니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "성공적으로 클럽 참여 취소함"),
            ApiResponse(responseCode = "404", description = "해당 클럽을 찾을 수 없음")
        ]
    )
    @PatchMapping("/{clubId}/join/cancel")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun cancelRequest(@PathVariable clubId: Long) {
        val userId = userPrincipalResolver.getUserId()
        clubJoinService.cancelRequest(userId, clubId)
    }

    @Operation(
        summary = "클럽 참여 신청 승인",
        description = "관리자가 클럽 참여 신청을 승인합니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "성공적으로 클럽 참여 신청을 승인함"),
            ApiResponse(responseCode = "404", description = "해당 클럽 또는 참가자를 찾을 수 없음")
        ]
    )
    @PatchMapping("/{clubId}/participants/{approvedUserId}/approve")
    @ResponseStatus(HttpStatus.OK)
    fun approve(@PathVariable clubId: Long, @PathVariable approvedUserId: Long) {
        val userId = userPrincipalResolver.getUserId()
        clubJoinService.approve(userId, clubId, approvedUserId)
    }

    @Operation(
        summary = "클럽 참여 신청 거절",
        description = "관리자가 클럽 참여 신청을 거절합니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "성공적으로 클럽 참여 신청을 거절함"),
            ApiResponse(responseCode = "404", description = "해당 클럽 또는 참가자를 찾을 수 없음")
        ]
    )
    @PatchMapping("/{clubId}/participants/{rejectedUserId}/reject")
    @ResponseStatus(HttpStatus.OK)
    fun reject(@PathVariable clubId: Long, @PathVariable rejectedUserId: Long) {
        val userId = userPrincipalResolver.getUserId()
        clubJoinService.reject(userId, clubId, rejectedUserId)
    }

    @Operation(
        summary = "클럽 참여자 강퇴",
        description = "관리자가 클럽 참여자를 강퇴합니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "성공적으로 클럽 참여자를 강퇴함"),
            ApiResponse(responseCode = "404", description = "해당 클럽 또는 참가자를 찾을 수 없음")
        ]
    )
    @PatchMapping("/{clubId}/participants/{kickedUserId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun kick(@PathVariable clubId: Long, @PathVariable kickedUserId: Long) {
        val userId = userPrincipalResolver.getUserId()
        clubJoinService.kick(userId, clubId, kickedUserId)
    }

    @Operation(
        summary = "클럽 멤버 목록 조회",
        description = "특정 클럽에 속한 멤버 목록을 조회합니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "성공적으로 클럽 멤버 목록을 조회함"),
            ApiResponse(responseCode = "404", description = "해당 클럽을 찾을 수 없음")
        ]
    )
    @GetMapping("/{clubId}/members")
    @ResponseStatus(HttpStatus.OK)
    fun getClubMembers(@PathVariable clubId: Long): List<ClubMemberResponse> {
        return clubJoinService.getClubMembers(clubId)
    }
}
