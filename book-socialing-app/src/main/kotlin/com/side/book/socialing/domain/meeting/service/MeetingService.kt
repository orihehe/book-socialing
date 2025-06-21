package com.side.book.socialing.domain.meeting.service

import com.side.book.socialing.domain.meeting.command.CreateMeetingCommand
import com.side.book.socialing.domain.meeting.entity.Meeting
import com.side.book.socialing.domain.meeting.repository.MeetingRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MeetingService(
    private val meetingRepository: MeetingRepository,
) {
    @Transactional
    fun createMeeting(cmd: CreateMeetingCommand): Meeting {
        val meeting = Meeting.create(cmd)
        return meetingRepository.save(meeting)
    }
}
