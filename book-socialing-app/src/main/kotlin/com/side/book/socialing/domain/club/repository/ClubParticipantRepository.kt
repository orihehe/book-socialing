package com.side.book.socialing.domain.club.repository

import com.side.book.socialing.domain.note.entity.NoteParticipant
import org.springframework.data.jpa.repository.JpaRepository

interface ClubParticipantRepository: JpaRepository<NoteParticipant, Long> {
}
