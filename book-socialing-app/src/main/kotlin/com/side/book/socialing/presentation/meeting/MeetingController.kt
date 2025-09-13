package com.side.book.socialing.presentation.meeting

import com.side.book.socialing.domain.meeting.command.CreateMeetingCommand
import com.side.book.socialing.domain.meeting.service.MeetingService
import com.side.book.socialing.presentation.meeting.dto.CreateMeetingRequest
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/meeting/v1")
@RestController
class MeetingController(
    private val meetingService: MeetingService
) {
    @PostMapping("/create")
    fun create(
        @RequestBody request: CreateMeetingRequest
    ): Long {
        val command =
            CreateMeetingCommand(
                name = request.name,
                description = request.description,
                bookName = request.bookName,
                bookAuthor = request.bookAuthor,
                bookLink = request.bookLink,
                meetDate = request.meetDate,
                round = request.round,
                // TODO: 로그인 유저로 대체
                createdBy = "anonymous"
            )

        return meetingService.createMeeting(command).id!!
    }
}
