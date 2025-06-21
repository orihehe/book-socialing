package com.side.book.socialing.domain.meeting.repository

import com.side.book.socialing.domain.meeting.entity.Meeting
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MeetingRepository : JpaRepository<Meeting, Long>
