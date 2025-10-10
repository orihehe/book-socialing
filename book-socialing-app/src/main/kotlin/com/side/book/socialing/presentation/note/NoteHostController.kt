package com.side.book.socialing.presentation.note

import com.side.book.socialing.domain.note.service.NoteHostService
import com.side.book.socialing.domain.user.dto.UserDto
import com.side.book.socialing.global.auth.UserPrincipalResolver
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Tag(name = "노트 관리자 API", description = "노트 관리를 위한 API")
@RestController
@RequestMapping("/api/v1/note")
class NoteHostController(
    private val noteHostService: NoteHostService,
    private val userPrincipalResolver: UserPrincipalResolver
) {

    @GetMapping("/guests")
    @Operation(
        summary = "노트 게스트 조회"
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "성공"),
            ApiResponse(responseCode = "404", description = "아이디에 해당하는 노트 없음")
        ]
    )
    fun getGuests(
        @RequestParam noteId: Long
    ): List<UserDto> {
        val userId = userPrincipalResolver.getUserId()
        return noteHostService.getGuests(userId, noteId)
    }
}
