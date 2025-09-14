package com.side.book.socialing.global.test

import com.side.book.socialing.domain.note.command.CreateNoteCommand
import com.side.book.socialing.domain.note.service.NoteJoinService
import com.side.book.socialing.domain.note.service.NoteService
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Service
import java.io.InputStream
import java.time.LocalDateTime

// TODO: remove after integrate services
@Service
class TestService(
    private val noteService: NoteService,
    private val noteJoinService: NoteJoinService
) {
    companion object {
        private const val HOST_ID_1 = 1L
        private const val HOST_ID_2 = 2L
    }

    fun createTestData() {
        val imageResource = ClassPathResource("static/images/default_book_image.jpg")
        val imageStream: InputStream = imageResource.inputStream

        val noteId = noteService.createNote(
            CreateNoteCommand(
                userId = HOST_ID_1,
                clubId = null,
                bookName = "가지가지 나뭇가지",
                bookAuthor = "이주예",
                description = "이주예의 대명사는 이주예, 이주예아니고 주예. 주예하다.",
                startAt = LocalDateTime.now(),
                endAt = LocalDateTime.now().plusDays(5),
                imageFiles = listOf()
            )
        )

        noteJoinService.joinRequest(22L, noteId)
        noteJoinService.joinRequest(33L, noteId)
        noteJoinService.joinRequest(44L, noteId)
        noteJoinService.joinRequest(55L, noteId)

        noteJoinService.approve(HOST_ID_1, noteId, 22L)
        noteJoinService.approve(HOST_ID_1, noteId, 33L)

        val noteId2 = noteService.createNote(
            CreateNoteCommand(
                userId = HOST_ID_2,
                clubId = null,
                bookName = "가지가지 나뭇가지 2",
                bookAuthor = "2주예",
                description = "2주예의 대명사는 2주예, 2주예아니고 주예. 주예하다.",
                startAt = LocalDateTime.now(),
                endAt = LocalDateTime.now().plusDays(5),
                imageFiles = listOf()
            )
        )
    }
}
