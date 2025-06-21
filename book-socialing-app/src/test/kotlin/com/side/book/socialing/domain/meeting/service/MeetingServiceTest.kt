package com.side.book.socialing.domain.meeting.service

import com.side.book.socialing.domain.meeting.command.CreateMeetingCommand
import com.side.book.socialing.domain.meeting.entity.Meeting
import com.side.book.socialing.domain.meeting.repository.MeetingRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class MeetingServiceTest {
    private lateinit var meetingRepository: MeetingRepository
    private lateinit var meetingService: MeetingService

    @BeforeEach
    fun setup() {
        meetingRepository = mockk()
        meetingService = MeetingService(meetingRepository)
    }

    @Test
    fun `createMeeting should save and return a meeting`() {
        // given
        val command =
            CreateMeetingCommand(
                name = "책 모임",
                description = "재밌는 책",
                bookName = "이펙티브 자바",
                bookAuthor = "조슈아 블로크",
                bookLink = "https://example.com",
                meetDate = LocalDateTime.of(2025, 7, 1, 19, 0),
                round = 1,
                createdBy = "tester",
            )

        val savedMeeting = Meeting.create(command).apply { id = 1L }

        every { meetingRepository.save(any()) } returns savedMeeting

        // when
        val result = meetingService.createMeeting(command)

        // then
        assertNotNull(result.id)
        assertEquals(command.name, result.name)
        assertEquals(command.bookName, result.bookName)
        assertEquals(1L, result.id)

        verify(exactly = 1) { meetingRepository.save(any()) }
    }
}
