package com.side.book.socialing.domain.chat.repository

import com.side.book.socialing.domain.chat.entity.ChatRoomParticipant
import org.springframework.data.jpa.repository.JpaRepository

interface ChatRoomParticipantRepository : JpaRepository<ChatRoomParticipant, Long> {
    fun findByChatRoomIdAndUserId(chatRoomId: Long, userId: Long): ChatRoomParticipant?
}
