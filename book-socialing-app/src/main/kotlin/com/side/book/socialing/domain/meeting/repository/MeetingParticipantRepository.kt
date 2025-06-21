package com.side.book.socialing.domain.meeting.repository

import com.side.book.socialing.domain.meeting.entity.MeetingParticipant
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MeetingParticipantRepository : JpaRepository<MeetingParticipant, Long>
