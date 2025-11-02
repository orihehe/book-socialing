package com.side.book.socialing.presentation.user

import com.side.book.socialing.domain.user.service.UserService
import com.side.book.socialing.global.auth.UserPrincipalResolver
import com.side.book.socialing.global.utils.log
import com.side.book.socialing.presentation.user.dto.UserResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "유저 API", description = "유저 관련 API")
@RestController
@RequestMapping("/api/v1/user")
class UserController (
    private val userService: UserService,
    private val userPrincipalResolver: UserPrincipalResolver
) {
    @Operation(
        summary = "로그인한 사용자 정보 조회",
        description = "현재 로그인한 사용자의 상세 정보(ID, 이메일, 닉네임, 역할)가 반환됩니다"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "성공적으로 로그인한 사용자 정보를 조회함",
            ),
            ApiResponse(
                responseCode = "404",
                description = "로그인한 사용자를 찾을 수 없음" // 404 응답 추가
            ),
            ApiResponse(
                responseCode = "500",
                description = "서버 내부 오류 발생"
            )
        ]
    )
    @GetMapping("/me")
    fun getUserProfileResponse(): ResponseEntity<UserResponse> {
        val userId = userPrincipalResolver.getUserId()

        return try {
            val user = userService.getUserProfileResponse(userId)
            if (user == null) { // 사용자를 찾지 못했을 경우
                ResponseEntity.notFound().build() // 404 Not Found
            } else {
                ResponseEntity.ok(user)
            }
        } catch (e: Exception) {
            log.error("Error fetching user profile for user $userId", e)
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .build()
        }
    }
}
