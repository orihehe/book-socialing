package com.side.book.socialing.presentation.club

import com.side.book.socialing.domain.club.command.CreateClubCommand
import com.side.book.socialing.domain.club.command.UpdateClubCommand
import com.side.book.socialing.domain.club.service.ClubService
import com.side.book.socialing.global.auth.UserPrincipalResolver
import com.side.book.socialing.global.utils.log
import com.side.book.socialing.presentation.club.dto.ClubPageResponse
import com.side.book.socialing.presentation.club.dto.CommonClubResponse
import com.side.book.socialing.presentation.club.dto.CreateClubRequest
import com.side.book.socialing.presentation.club.dto.SearchClubResponse
import com.side.book.socialing.presentation.club.dto.SingleClubResponse
import com.side.book.socialing.presentation.club.dto.UpdateClubRequest
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@Tag(name = "클럽 API", description = "클럽 생성, 조회, 수정, 삭제 관련 API")
@RestController
@RequestMapping("/api/v1/club")
class ClubController(
    private val clubService: ClubService,
    private val userPrincipalResolver: UserPrincipalResolver
) {

    @PostMapping("/create", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun createClub(
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
        summary = "클럽 단건 조회",
        description = "클럽 ID로 클럽 정보를 단건 조회합니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "성공적으로 클럽 정보를 조회함"),
            ApiResponse(responseCode = "404", description = "클럽을 찾을 수 없음"),
            ApiResponse(responseCode = "500", description = "서버 내부 오류")
        ]
    )
    @GetMapping("/{clubId}")
    fun getClubById(@PathVariable clubId: Long): ResponseEntity<SingleClubResponse> {
        val club = clubService.getClubById(clubId)
        return ResponseEntity.ok(club)
    }

    @DeleteMapping("/{clubId}")
    @Operation(
        summary = "클럽 삭제",
        description = "클럽 ID를 통해 특정 노트를 삭제합니다. 호스트만 삭제할 수 있습니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "클럽 삭제 성공")
        ]
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteNote(
        @PathVariable clubId: Long
    ): ResponseEntity<Void> {
        val userId = userPrincipalResolver.getUserId()
        clubService.deleteClub(clubId, userId)

        return ResponseEntity.noContent().build()
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
    fun getJoinedClubs(
        @RequestParam(defaultValue = "10") pageSize: Int,
        @RequestParam(defaultValue = "1") pageNum: Int
    ): ResponseEntity<ClubPageResponse<CommonClubResponse>> {
        val userId = userPrincipalResolver.getUserId()
        val offset = (pageNum - 1) * pageSize

        return try {
            val joinedClubs = clubService.getJoinedClubs(userId, pageSize, offset)
            ResponseEntity.ok(joinedClubs)
        } catch (e: Exception) {
            log.error("Error fetching joined clubs for user $userId", e)
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ClubPageResponse(totalCount = 0L, groups = emptyList())) // 빈 리스트 반환 또는 에러 DTO 반환
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
    fun getCreatedClubs(
        @RequestParam(defaultValue = "10") pageSize: Int,
        @RequestParam(defaultValue = "1") pageNum: Int
    ): ResponseEntity<ClubPageResponse<CommonClubResponse>> {
        val userId = userPrincipalResolver.getUserId()
        val offset = (pageNum - 1) * pageSize

        return try {
            val createdClubs = clubService.getCreatedClubs(userId, pageSize, offset)
            ResponseEntity.ok(createdClubs)
        } catch (e: Exception) {
            log.error("Error fetching created clubs for user $userId", e)
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ClubPageResponse(totalCount = 0L, groups = emptyList())) // 빈 리스트 반환 또는 에러 DTO 반환
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
    fun getRecommendedClubs(
        @RequestParam(defaultValue = "2") pageSize: Int,
        @RequestParam(defaultValue = "1") pageNum: Int
    ): ResponseEntity<ClubPageResponse<CommonClubResponse>> {
        val userId = userPrincipalResolver.getUserId()
        val offset = (pageNum - 1) * pageSize

        return try {
            val recommendClubs = clubService.getRecommendClubs(userId, pageSize, offset)
            ResponseEntity.ok(recommendClubs)
        } catch (e: Exception) {
            log.error("Error fetching recommend clubs for user $userId", e)
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ClubPageResponse(totalCount = 0L, groups = emptyList())) // 빈 리스트 반환 또는 에러 DTO 반환
        }
    }

    @PutMapping("/{clubId}", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    @Operation(
        summary = "클럽 수정",
        description = "클럽 ID를 통해 특정 클럽을 수정합니다. 호스트만 수정할 수 있습니다"
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "클럽 수정 성공")
        ]
    )
    fun updateClub(
        @PathVariable clubId: Long,
        @RequestPart("request") request: UpdateClubRequest,
        @RequestPart("images", required = false) imageFiles: List<MultipartFile>?,
        @RequestPart("deletedImageIds", required = false) deletedFileIds: List<Long>?
    ): ResponseEntity<Void> {
        val userId = userPrincipalResolver.getUserId()
        val command = UpdateClubCommand(
            clubId = clubId,
            userId = userId,
            clubName = request.clubName,
            description = request.description,
            imageFiles = imageFiles ?: emptyList(),
            deletedFileIds = deletedFileIds ?: emptyList()
        )

        clubService.updateClub(command)
        return ResponseEntity.ok().build()
    }

    @Operation(
        summary = "클럽 검색",
        description = "클럽 이름 또는 설명으로 클럽을 검색합니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "성공적으로 클럽 목록을 조회함"),
            ApiResponse(responseCode = "500", description = "서버 내부 오류")
        ]
    )
    @GetMapping("/search")
    fun searchClubs(
        @RequestParam query: String, // 검색어
        @RequestParam(defaultValue = "10") pageSize: Int,
        @RequestParam(defaultValue = "1") pageNum: Int
    ): ResponseEntity<ClubPageResponse<SearchClubResponse>> {
        val userId = userPrincipalResolver.getUserId()
        val offset = (pageNum - 1) * pageSize

        return try {
            val searchResults = clubService.searchClub(userId, query, pageSize, offset)
            ResponseEntity.ok(searchResults)
        } catch (e: Exception) {
            log.error("Error searching clubs with query: $query", e)
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ClubPageResponse(totalCount = 0L, groups = emptyList()))
        }
    }
}
