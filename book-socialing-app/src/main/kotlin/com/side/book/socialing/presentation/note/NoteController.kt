package com.side.book.socialing.presentation.note

import com.side.book.socialing.presentation.note.dto.CommonNoteResponse
import com.side.book.socialing.presentation.note.dto.OpenNoteResponse
import com.side.book.socialing.presentation.note.dto.ParticipantInfoResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@Tag(name = "노트 API", description = "노트 조회, 생성, 참여, 퇴고 등 노트 관련 API")
@RestController
@RequestMapping("/api/note/v1")
class NoteController (
    // private val noteService: NoteService,
) {
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
            // val openNotes = noteService.getOpenNotes(userId)
            // ResponseEntity.ok(openNotes)

            // 실제 DB 호출 로직 대신 더미 데이터 생성
            val dummyNotes = mutableListOf<OpenNoteResponse>()

            // 첫 번째 더미 노트
            dummyNotes.add(
                OpenNoteResponse(
                    id = 101L,
                    clubName = "더미 독서모임 A",
                    bookName = "더미 데미안",
                    bookAuthor = "더미 헤르만 헤세",
                    bookImageUrl = "https://contents.kyobobook.co.kr/sih/fit-in/458x0/pdt/9791164053353.jpg", // 더미 이미지 URL
                    description = "더미 데이터로 채워진 '데미안' 토론 모임입니다.",
                    participantList = listOf(
                        ParticipantInfoResponse(
                            participantId = 1L,
                            userId = userId, // 요청된 userId가 HOST인 것으로 가정
                            username = "더미유저_${userId}_HOST",
                            role = "HOST",
                            status = "JOINED"
                        ),
                        ParticipantInfoResponse(
                            participantId = 2L,
                            userId = 99L,
                            username = "더미유저_99_MEMBER",
                            role = "MEMBER",
                            status = "JOINED"
                        )
                    ),
                    startDateTime = LocalDateTime.of(2025, 8, 1, 19, 0),
                    endDateTime = LocalDateTime.of(2025, 8, 1, 21, 0),
                )
            )

            // 두 번째 더미 노트
            dummyNotes.add(
                OpenNoteResponse(
                    id = 102L,
                    clubName = null, // 클럽이 없는 더미 노트
                    bookName = "더미 어린 왕자",
                    bookAuthor = "더미 앙투안 드 생텍쥐페리",
                    bookImageUrl = "https://contents.kyobobook.co.kr/sih/fit-in/458x0/pdt/9791164455300.jpg", // 또 다른 더미 이미지 URL
                    description = "더미 데이터로 채워진 '어린 왕자' 자유 토론",
                    participantList = listOf(
                        ParticipantInfoResponse(
                            participantId = 3L,
                            userId = userId, // 요청된 userId가 MEMBER인 것으로 가정
                            username = "더미유저_${userId}_MEMBER",
                            role = "MEMBER",
                            status = "JOINED"
                        ),
                        ParticipantInfoResponse(
                            participantId = 4L,
                            userId = 98L,
                            username = "더미유저_98_HOST",
                            role = "HOST",
                            status = "JOINED"
                        )
                    ),
                    startDateTime = LocalDateTime.of(2025, 8, 15, 14, 0),
                    endDateTime = LocalDateTime.of(2025, 8, 15, 16, 0),
                )
            )


            // ===== 세 번째 더미 노트 =====
            dummyNotes.add(
                OpenNoteResponse(
                    id = 103L,
                    clubName = "더미 SF 클럽",
                    bookName = "더미 은하수를 여행하는 히치하이커를 위한 안내서",
                    bookAuthor = "더미 더글러스 애덤스",
                    bookImageUrl = "https://contents.kyobobook.co.kr/sih/fit-in/458x0/pdt/9788970135472.jpg",
                    description = "더미 SF 소설 토론 모임입니다.",
                    participantList = listOf(
                        ParticipantInfoResponse(
                            participantId = 5L,
                            userId = 97L,
                            username = "더미유저_97_HOST",
                            role = "HOST",
                            status = "JOINED"
                        ),
                        ParticipantInfoResponse(
                            participantId = 6L,
                            userId = userId,
                            username = "더미유저_${userId}_MEMBER",
                            role = "MEMBER",
                            status = "JOINED"
                        ),
                        ParticipantInfoResponse(
                            participantId = 7L,
                            userId = 96L,
                            username = "더미유저_96_MEMBER",
                            role = "MEMBER",
                            status = "JOINED"
                        )
                    ),
                    startDateTime = LocalDateTime.of(2025, 9, 1, 10, 0),
                    endDateTime = LocalDateTime.of(2025, 9, 1, 12, 0),
                )
            )

            // ===== 네 번째 더미 노트 =====
            dummyNotes.add(
                OpenNoteResponse(
                    id = 104L,
                    clubName = "더미 자기계발 그룹",
                    bookName = "더미 역행자",
                    bookAuthor = "더미 자청",
                    bookImageUrl = "https://contents.kyobobook.co.kr/sih/fit-in/458x0/pdt/9788901272580.jpg",
                    description = "더미 인생을 변화시키는 독서 모임",
                    participantList = listOf(
                        ParticipantInfoResponse(
                            participantId = 8L,
                            userId = userId,
                            username = "더미유저_${userId}_HOST",
                            role = "HOST",
                            status = "JOINED"
                        )
                    ),
                    startDateTime = LocalDateTime.of(2025, 9, 10, 20, 0),
                    endDateTime = LocalDateTime.of(2025, 9, 10, 22, 0),
                )
            )

            // ===== 다섯 번째 더미 노트 =====
            dummyNotes.add(
                OpenNoteResponse(
                    id = 105L,
                    clubName = null,
                    bookName = "더미 코스모스",
                    bookAuthor = "더미 칼 세이건",
                    bookImageUrl = "https://contents.kyobobook.co.kr/sih/fit-in/458x0/pdt/9788983711892.jpg",
                    description = "더미 우주에 대한 탐구와 토론",
                    participantList = listOf(
                        ParticipantInfoResponse(
                            participantId = 9L,
                            userId = 95L,
                            username = "더미유저_95_MEMBER",
                            role = "MEMBER",
                            status = "JOINED"
                        ),
                        ParticipantInfoResponse(
                            participantId = 10L,
                            userId = userId,
                            username = "더미유저_${userId}_MEMBER",
                            role = "MEMBER",
                            status = "JOINED"
                        )
                    ),
                    startDateTime = LocalDateTime.of(2025, 9, 20, 16, 0),
                    endDateTime = LocalDateTime.of(2025, 9, 20, 18, 0),
                )
            )

            ResponseEntity.ok(dummyNotes)
        } catch (e: Exception) {
            System.err.println("Error fetching open notes for user $userId: ${e.message}") // 에러 로깅
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR) // 500 Internal Server Error
                .body(emptyList()) // 빈 리스트 반환 또는 에러 DTO 반환
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
                    endDateTime = LocalDateTime.of(2025, 8, 1, 21, 0),
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
                    endDateTime = LocalDateTime.of(2025, 8, 15, 16, 0),
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
                    endDateTime = LocalDateTime.of(2025, 8, 1, 21, 0),
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
                    endDateTime = LocalDateTime.of(2025, 8, 15, 16, 0),
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
                    endDateTime = LocalDateTime.of(2025, 8, 1, 21, 0),
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
                    endDateTime = LocalDateTime.of(2025, 8, 15, 16, 0),
                )
            )

            ResponseEntity.ok(dummyNotes)
        } catch (e: Exception) {
            System.err.println("Error fetching recommended notes for user $userId: ${e.message}") // 에러 로깅
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR) // 500 Internal Server Error
                .body(emptyList()) // 빈 리스트 반환 또는 에러 DTO 반환
        }
    }

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
                    endDateTime = LocalDateTime.of(2025, 8, 1, 21, 0),
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
                    endDateTime = LocalDateTime.of(2025, 8, 15, 16, 0),
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
