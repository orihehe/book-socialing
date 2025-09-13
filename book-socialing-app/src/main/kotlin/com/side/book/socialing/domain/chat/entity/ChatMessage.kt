package com.side.book.socialing.domain.chat.entity

import com.side.book.socialing.domain.common.BaseEntity
import jakarta.persistence.*

@Entity
@Table(name = "chat_messages")
class ChatMessage(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    val chatRoom: ChatRoom,

    @Column(name = "sender_id", nullable = false)
    val senderId: Long,

    @Column(columnDefinition = "TEXT", nullable = false)
    val content: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val messageType: MessageType = MessageType.GENERAL

) : BaseEntity() {

    companion object {
        fun create(chatRoom: ChatRoom, senderId: Long, content: String, messageType: MessageType): ChatMessage {
            return ChatMessage(
                chatRoom = chatRoom,
                senderId = senderId,
                content = content,
                messageType = messageType
            )
        }
    }
}
