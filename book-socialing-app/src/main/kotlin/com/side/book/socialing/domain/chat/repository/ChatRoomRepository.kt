package com.side.book.socialing.domain.chat.repository

import com.side.book.socialing.domain.chat.entity.ChatRoom
import org.springframework.data.jpa.repository.JpaRepository

interface ChatRoomRepository : JpaRepository<ChatRoom, Long> {
    fun findByNoteId(noteId: Long): ChatRoom?
}
