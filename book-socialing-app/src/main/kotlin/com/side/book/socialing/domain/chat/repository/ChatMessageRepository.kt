package com.side.book.socialing.domain.chat.repository

import com.side.book.socialing.domain.chat.entity.ChatMessage
import com.side.book.socialing.domain.chat.entity.MessageType
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.jpa.repository.JpaRepository

interface ChatMessageRepository : JpaRepository<ChatMessage, Long> {
    fun findByChatRoomIdAndMessageTypeAndIdLessThanOrderByIdDesc(
        chatRoomId: Long,
        messageType: MessageType,
        id: Long,
        pageable: Pageable
    ): Slice<ChatMessage>
}
