package com.side.book.socialing.presentation.club

import com.side.book.socialing.presentation.club.dto.CommonClubResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "클럽 API", description = "클럽 생성, 조회, 수정, 삭제 관련 API")
@RestController
@RequestMapping("/api/club/v1")
class ClubController() {

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
        // TODO: 회원 정보 필요
        val userId = 123L

        return try {
            // 실제 DB 호출 로직 대신 더미 데이터 생성
            val dummyClubs = mutableListOf<CommonClubResponse>()

            dummyClubs.add(
                CommonClubResponse(
                    id = 10L,
                    clubName = "내가 속한 독서 클럽 '고전 읽기'",
                    clubImageUrl = "https://dummyimage.com/600x400/007bff/fff&text=Club1",
                    description = "매주 고전 문학 작품을 읽고 토론하는 모임입니다.",
                    memberCount = 15
                )
            )

            dummyClubs.add(
                CommonClubResponse(
                    id = 11L,
                    clubName = "내가 속한 소설 창작 클럽 '이야기꾼들'",
                    clubImageUrl = "https://dummyimage.com/600x400/28a745/fff&text=Club2",
                    description = "자신만의 이야기를 만들고 서로 피드백하는 클럽입니다.",
                    memberCount = 7
                )
            )

            dummyClubs.add(
                CommonClubResponse(
                    id = 12L,
                    clubName = "내가 속한 SF/판타지 클럽 '은하수 너머'",
                    clubImageUrl = "https://dummyimage.com/600x400/ffc107/333&text=Club3",
                    description = "흥미진진한 SF와 판타지 소설을 함께 탐험합니다.",
                    memberCount = 22
                )
            )

            dummyClubs.add(
                CommonClubResponse(
                    id = 13L,
                    clubName = "내가 속한 에세이 & 산문 클럽 '마음을 담다'",
                    clubImageUrl = "https://dummyimage.com/600x400/dc3545/fff&text=Club4",
                    description = "일상의 단상을 글로 쓰고 나누는 편안한 모임입니다.",
                    memberCount = 10
                )
            )

            dummyClubs.add(
                CommonClubResponse(
                    id = 14L,
                    clubName = "내가 속한 인문학 클럽 '지혜의 숲'",
                    clubImageUrl = "https://dummyimage.com/600x400/6f42c1/fff&text=Club5",
                    description = "철학, 역사, 예술 등 다양한 인문학 주제로 깊이 있는 대화를 나눕니다.",
                    memberCount = 18
                )
            )

            ResponseEntity.ok(dummyClubs)
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
        // TODO: 회원 정보 필요
        val userId = 123L

        return try {
            val dummyClubs = mutableListOf<CommonClubResponse>()

            dummyClubs.add(
                CommonClubResponse(
                    id = 20L,
                    clubName = "내가 만든 문학 비평 클럽 '텍스트 너머'",
                    clubImageUrl = "https://dummyimage.com/600x400/8c52ff/fff&text=CreatedClub1",
                    description = "다양한 문학 작품을 비판적으로 분석하고 토론하는 클럽입니다.",
                    memberCount = 8
                )
            )

            dummyClubs.add(
                CommonClubResponse(
                    id = 21L,
                    clubName = "내가 만든 자기계발 독서 클럽 '성장 동력'",
                    clubImageUrl = "https://dummyimage.com/600x400/fd7e14/fff&text=CreatedClub2",
                    description = "자기계발 서적을 읽고 실천하며 함께 성장하는 모임입니다.",
                    memberCount = 12
                )
            )

            dummyClubs.add(
                CommonClubResponse(
                    id = 22L,
                    clubName = "내가 만든 역사 탐구 클럽 '과거의 발자취'",
                    clubImageUrl = "https://dummyimage.com/600x400/6610f2/fff&text=CreatedClub3",
                    description = "세계사와 한국사의 흥미로운 순간들을 깊이 있게 탐구합니다.",
                    memberCount = 6
                )
            )

            dummyClubs.add(
                CommonClubResponse(
                    id = 23L,
                    clubName = "내가 만든 시 쓰기 클럽 '언어의 정원'",
                    clubImageUrl = "https://dummyimage.com/600x400/e83e8c/fff&text=CreatedClub4",
                    description = "자유롭게 시를 쓰고 서로의 작품을 감상하며 영감을 나눕니다.",
                    memberCount = 9
                )
            )

            ResponseEntity.ok(dummyClubs)
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
    @GetMapping("/recommended")
    fun getRecommendedClubs(): ResponseEntity<List<CommonClubResponse>> {
        // TODO: 회원 정보 필요
        val userId = 123L

        return try {
            val dummyClubs = mutableListOf<CommonClubResponse>()

            dummyClubs.add(
                CommonClubResponse(
                    id = 30L,
                    clubName = "추천하는 세계 문학 독서 클럽 '글로벌 북스'",
                    clubImageUrl = "https://dummyimage.com/600x400/17a2b8/fff&text=Recommended1",
                    description = "전 세계의 다양한 문학 작품을 깊이 있게 다루는 클럽입니다. 현재 인기 급상승 중!",
                    memberCount = 35
                )
            )

            dummyClubs.add(
                CommonClubResponse(
                    id = 31L,
                    clubName = "추천하는 베스트셀러 독파 클럽 '북마스터'",
                    clubImageUrl = "https://dummyimage.com/600x400/6c757d/fff&text=Recommended2",
                    description = "최신 베스트셀러를 매주 선정하여 함께 읽고 감상을 공유합니다. 활발한 토론!",
                    memberCount = 42
                )
            )

            ResponseEntity.ok(dummyClubs)
        } catch (e: Exception) {
            System.err.println("Error fetching recommended clubs for user $userId: ${e.message}") // 에러 로깅
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR) // 500 Internal Server Error
                .body(emptyList()) // 빈 리스트 반환 또는 에러 DTO 반환
        }
    }
}
