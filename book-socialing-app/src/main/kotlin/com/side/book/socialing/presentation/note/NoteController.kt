package com.side.book.socialing.presentation.note

import com.side.book.socialing.presentation.note.dto.GetCurrentNoteResponse
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@RestController
@RequestMapping("/note/v1")
class NoteController {

    @GetMapping("/current")
    fun getCurrentNote(): GetCurrentNoteResponse {
        return GetCurrentNoteResponse(
            id = 1,
            clubName = "saisai",
            bookName = "두 개의 탑",
            bookAuthor = "JRR Tolkein",
            bookImageUrl = "https://covers.openlibrary.org/b/id/8231856-L.jpg",
            description = "~클럽의 몇번째 책임입니다",
            participantCount = 10,
            startDateTime = LocalDateTime.of(2025, 6, 22, 0, 0),
            endDateTime = LocalDateTime.of(2025, 6, 28, 0, 0)
        )
    }
}
