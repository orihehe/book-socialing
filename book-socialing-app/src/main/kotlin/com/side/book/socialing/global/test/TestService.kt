package com.side.book.socialing.global.test

import com.side.book.socialing.domain.club.repository.ClubParticipantRepository
import com.side.book.socialing.domain.note.command.CreateNoteCommand
import com.side.book.socialing.domain.note.repository.NoteParticipantRepository
import com.side.book.socialing.domain.note.service.NoteJoinService
import com.side.book.socialing.domain.note.service.NoteService
import com.side.book.socialing.domain.user.command.CreateUserCommand
import com.side.book.socialing.domain.user.entity.User
import com.side.book.socialing.domain.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

// TODO: remove after integrate services
@Service
class TestService(
    private val noteService: NoteService,
    private val noteJoinService: NoteJoinService,
    private val userRepository: UserRepository,
    private val noteParticipantRepository: NoteParticipantRepository,
    private val clubParticipantRepository: ClubParticipantRepository
) {
    companion object {
        private const val HOST_ID_1 = 1L
        private const val HOST_ID_2 = 2L
    }

    fun createNoteData() {
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

        noteService.createNote(
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

    @Transactional
    fun createUserData() {
        userRepository.deleteAll()
        userRepository.flush()

        val existUserIds = (
            noteParticipantRepository.findAll().map { it.userId } +
                clubParticipantRepository.findAll().map { it.userId }
            ).toSet()

        val users = existUserIds.mapIndexed { index, it ->
            User.create(
                CreateUserCommand(
                    provider = "KAKAO",
                    providerId = index.toString(),
                    email = "test$index@email.com",
                    nickName = "testName$index",
                    description = "나는야 개발자",
                    role = "ROLE_USER"
                )
            )
        }

        userRepository.saveAll(users)
    }
}
