package com.side.book.socialing.presentation.note

import com.side.book.socialing.domain.enums.ParticipantRole
import com.side.book.socialing.domain.enums.ParticipantStatus
import com.side.book.socialing.domain.note.service.NoteService
import com.side.book.socialing.presentation.note.dto.CreatedNotesResponse
import com.side.book.socialing.presentation.note.dto.OpenNotesResponse
import com.side.book.socialing.presentation.note.dto.ParticipantInfoResponse
import com.side.book.socialing.presentation.note.dto.PendingNotesResponse
import com.side.book.socialing.presentation.note.dto.RecommendedNotesResponse
import com.side.book.socialing.presentation.note.dto.RevisedNotesResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/note/v1")
class NoteController (
    private val noteService: NoteService,
) {
    @GetMapping("/open")
    fun getOpenNotes(): ResponseEntity<List<OpenNotesResponse>> {

        // TODO: 회원 정보 필요
        val userId = 123L

        return try {
            val openNotes = noteService.getOpenNotes(userId)
            ResponseEntity.ok(openNotes)
        } catch (e: Exception) {
            System.err.println("Error fetching open notes for user $userId: ${e.message}") // 에러 로깅
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR) // 500 Internal Server Error
                .body(emptyList()) // 빈 리스트 반환 또는 에러 DTO 반환
        }
    }

    @GetMapping("/created")
    fun getCreatedNotes(): ResponseEntity<List<CreatedNotesResponse>> {

        // TODO: 회원 정보 필요
        val userId = 123L

        return try {
            // val createdNotes = noteService.getCreatedNotes(userId)

            val dummyNotes = mutableListOf<CreatedNotesResponse>()

            // 첫 번째 더미 노트
            dummyNotes.add(
                CreatedNotesResponse(
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
                CreatedNotesResponse(
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

    @GetMapping("/pending")
    fun getPendingNotes(): ResponseEntity<List<PendingNotesResponse>> {

        // TODO: 회원 정보 필요
        val userId = 123L

        return try {
            // val pendingNotes = noteService.getPendingNotes(userId)

            val dummyNotes = mutableListOf<PendingNotesResponse>()

            // 첫 번째 더미 노트
            dummyNotes.add(
                PendingNotesResponse(
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
                PendingNotesResponse(
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

    @GetMapping("/recommended")
    fun getRecommendedNotes(): ResponseEntity<List<RecommendedNotesResponse>> {

        // TODO: 회원 정보 필요
        val userId = 123L

        return try {
            // val recommendedNotes = noteService.getRecommendedNotes(userId)

            val dummyNotes = mutableListOf<RecommendedNotesResponse>()

            // 첫 번째 더미 노트
            dummyNotes.add(
                RecommendedNotesResponse(
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
                RecommendedNotesResponse(
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
    fun getRevisedNotes(): ResponseEntity<List<RevisedNotesResponse>> {

        // TODO: 회원 정보 필요
        val userId = 123L

        return try {
            // val revisedNotes = noteService.getParticipatedRevisedNotes(userId)

            val dummyNotes = mutableListOf<RevisedNotesResponse>()

            // 첫 번째 더미 노트
            dummyNotes.add(
                RevisedNotesResponse(
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
                RevisedNotesResponse(
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
