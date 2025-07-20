package com.side.book.socialing.presentation.note

import com.side.book.socialing.domain.note.service.NoteService
import com.side.book.socialing.presentation.note.dto.OpenNotesResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/note/v1")
class NoteController (
    private val noteService: NoteService,
) {
    @GetMapping("/open")
    fun getOpenNotes(): ResponseEntity<List<OpenNotesResponse>> {

        // TODO: 회원 정보 필요
        val userId = 123L;

        return try {
            val openNotes = noteService.getParticipatedOpenNotes(userId);
            ResponseEntity.ok(openNotes) // HTTP 200 OK와 함께 조회된 노트 목록 반환
        } catch (e: Exception) {
            System.err.println("Error fetching open notes for user $userId: ${e.message}") // 에러 로깅
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR) // 500 Internal Server Error
                .body(emptyList()) // 빈 리스트 반환 또는 에러 DTO 반환
        }
    }
}
