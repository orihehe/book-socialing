package com.side.book.socialing.domain.note.service

import com.side.book.socialing.domain.club.repository.ClubRepository
import com.side.book.socialing.domain.note.entity.Note
import com.side.book.socialing.domain.note.entity.NoteParticipant
import com.side.book.socialing.domain.note.enum.ParticipantRole
import com.side.book.socialing.domain.note.enum.ParticipantStatus
import com.side.book.socialing.domain.note.repository.NoteFileRepository
import com.side.book.socialing.domain.note.repository.NoteParticipantRepository
import com.side.book.socialing.domain.note.repository.NoteRepository
import com.side.book.socialing.global.file.FileUploader
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.slot
import io.mockk.verify
import jakarta.persistence.EntityNotFoundException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.context.ApplicationEventPublisher
import java.time.LocalDateTime
import java.util.*

@ExtendWith(MockKExtension::class)
class NoteServiceTest {

    private lateinit var noteService: NoteService

    private lateinit var noteJoinService: NoteJoinService

    @MockK
    private lateinit var noteRepository: NoteRepository

    @MockK
    private lateinit var noteParticipantRepository: NoteParticipantRepository

    @MockK(relaxed = true)
    private lateinit var noteFileRepository: NoteFileRepository

    @MockK(relaxed = true)
    private lateinit var clubRepository: ClubRepository

    @MockK(relaxed = true)
    private lateinit var fileUploader: FileUploader

    @MockK(relaxed = true)
    private lateinit var applicationEventPublisher: ApplicationEventPublisher

    private lateinit var note: Note

    @BeforeEach
    fun setUp() {
        noteService = NoteService(
            noteRepository = noteRepository,
            noteFileRepository = noteFileRepository,
            clubRepository = clubRepository,
            noteParticipantRepository = noteParticipantRepository,
            fileUploader = fileUploader,
            applicationEventPublisher = applicationEventPublisher,
            filePath = "/test/path"
        )

        note = Note(
            id = 1L,
            bookName = "Test Book",
            bookAuthor = "Test Author",
            startAt = LocalDateTime.now(),
            endAt = LocalDateTime.now().plusHours(2)
        )
    }

    @Test
    @DisplayName("사용자가 노트에 성공적으로 참여한다")
    fun testJoinNote_Success() {
        // Given
        val userId = 1L
        val noteId = note.id!!
        val participantSlot = slot<NoteParticipant>()

        every { noteRepository.findById(noteId) } returns Optional.of(note)
        every { noteParticipantRepository.findByNoteIdAndUserId(noteId, userId) } returns null
        every { noteParticipantRepository.save(capture(participantSlot)) } returns NoteParticipant(1L, note, userId)

        // When
        noteJoinService.joinRequest(userId, noteId)

        // Then
        verify(exactly = 1) { noteParticipantRepository.save(any()) }
        val savedParticipant = participantSlot.captured
        assertEquals(note, savedParticipant.note)
        assertEquals(userId, savedParticipant.userId)
        assertEquals(ParticipantRole.MEMBER, savedParticipant.role)
        assertEquals(ParticipantStatus.JOINED, savedParticipant.status)
    }

    @Test
    @DisplayName("참여하려는 노트가 존재하지 않으면 EntityNotFoundException이 발생한다")
    fun testJoinNote_NoteNotFound() {
        // Given
        val userId = 1L
        val noteId = 999L

        every { noteRepository.findById(noteId) } returns Optional.empty()

        // When & Then
        assertThrows<EntityNotFoundException> {
            noteJoinService.joinRequest(userId, noteId)
        }
        verify(exactly = 0) { noteParticipantRepository.save(any()) }
    }

    @Test
    @DisplayName("사용자가 이미 노트에 참여하고 있으면 IllegalStateException이 발생한다")
    fun testJoinNote_AlreadyParticipant() {
        // Given
        val userId = 1L
        val noteId = note.id!!
        val existingParticipant = NoteParticipant(1L, note, userId, ParticipantRole.MEMBER, ParticipantStatus.JOINED)

        every { noteRepository.findById(noteId) } returns Optional.of(note)
        every { noteParticipantRepository.findByNoteIdAndUserId(noteId, userId) } returns existingParticipant

        // When & Then
        assertThrows<IllegalStateException> {
            noteJoinService.joinRequest(userId, noteId)
        }
        verify(exactly = 0) { noteParticipantRepository.save(any()) }
    }
}
