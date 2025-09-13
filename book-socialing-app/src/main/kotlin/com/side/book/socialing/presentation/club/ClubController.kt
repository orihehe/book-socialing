package com.side.book.socialing.presentation.club

import com.side.book.socialing.domain.club.command.CreateClubCommand
import com.side.book.socialing.domain.club.service.ClubService
import com.side.book.socialing.global.auth.UserPrincipalResolver
import com.side.book.socialing.presentation.club.dto.CommonClubResponse
import com.side.book.socialing.presentation.club.dto.CreateClubRequest
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@Tag(name = "클럽 API", description = "클럽 생성, 조회, 수정, 삭제 관련 API")
@RestController
@RequestMapping("/api/club/v1")
class ClubController(
    private val clubService: ClubService,
    private val userPrincipalResolver: UserPrincipalResolver
) {

    @PostMapping("/create", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun createNote(
        @RequestPart("request") request: CreateClubRequest,
        @RequestPart("images") imageFiles: List<MultipartFile>
    ): Long {
        val userId = userPrincipalResolver.getUserId()

        val command =
            CreateClubCommand(
                userId = userId,
                clubName = request.clubName,
                description = request.description,
                imageFiles = imageFiles
            )

        return clubService.createClub(command).id!!
    }

    @Operation(
        summary = "로그인한 사용자가 속한 클럽 목록 조회",
        description = "현재 로그인한 사용자가 속한 클럽 목록을 조회합니다. 각 클럽의 기본 정보가 반환됩니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "성공적으로 클럽 목록을 조회함"),
            ApiResponse(responseCode = "500", description = "서버 내부 오류")
        ]
    )
    @GetMapping("/joined")
    fun getJoinedClubs(): ResponseEntity<List<CommonClubResponse>> {
        val userId = userPrincipalResolver.getUserId()

        return try {
            val joinedClubs = clubService.getJoinedClubs(userId)
            ResponseEntity.ok(joinedClubs)
        } catch (e: Exception) {
            System.err.println("Error fetching joined clubs for user $userId: ${e.message}") // 에러 로깅
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR) // 500 Internal Server Error
                .body(emptyList()) // 빈 리스트 반환 또는 에러 DTO 반환
        }
    }

    @Operation(
        summary = "로그인한 사용자가 만든 클럽 목록 조회",
        description = "현재 로그인한 사용자가 만든 클럽 목록을 조회합니다. 각 클럽의 기본 정보가 반환됩니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "성공적으로 클럽 목록을 조회함"),
            ApiResponse(responseCode = "500", description = "서버 내부 오류")
        ]
    )
    @GetMapping("/created")
    fun getCreatedClubs(): ResponseEntity<List<CommonClubResponse>> {
        val userId = userPrincipalResolver.getUserId()

        return try {
            val createdClubs = clubService.getCreatedClubs(userId)
            ResponseEntity.ok(createdClubs)
        } catch (e: Exception) {
            System.err.println("Error fetching created clubs for user $userId: ${e.message}") // 에러 로깅
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR) // 500 Internal Server Error
                .body(emptyList()) // 빈 리스트 반환 또는 에러 DTO 반환
        }
    }

    @Operation(
        summary = "추천 클럽 목록 조회",
        description = "로그인한 사용자에게 추천하는 클럽 목록을 조회합니다. 각 클럽의 기본 정보가 반환됩니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "성공적으로 클럽 목록을 조회함"),
            ApiResponse(responseCode = "500", description = "서버 내부 오류")
        ]
    )
    @GetMapping("/recommend")
    fun getRecommendedClubs(): ResponseEntity<List<CommonClubResponse>> {
        val userId = userPrincipalResolver.getUserId()

        return try {
            val recommendClubs = clubService.getRecommendClubs(userId)
            ResponseEntity.ok(recommendClubs)
        } catch (e: Exception) {
            System.err.println("Error fetching recommend clubs for user $userId: ${e.message}") // 에러 로깅
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR) // 500 Internal Server Error
                .body(emptyList()) // 빈 리스트 반환 또는 에러 DTO 반환
        }
    }
}
