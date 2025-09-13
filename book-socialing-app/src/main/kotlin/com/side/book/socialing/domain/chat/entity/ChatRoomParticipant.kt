package com.side.book.socialing.domain.chat.entity

import com.side.book.socialing.domain.common.BaseEntity
import jakarta.persistence.*

@Entity
@Table(name = "chat_room_participants")
class ChatRoomParticipant(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    val chatRoom: ChatRoom,

    @Column(name = "user_id", nullable = false)
    val userId: Long,

    @Column(name = "last_read_message_id")
    var lastReadMessageId: Long? = null

) : BaseEntity() {

    companion object {
        fun create(chatRoom: ChatRoom, userId: Long): ChatRoomParticipant {
            return ChatRoomParticipant(
                chatRoom = chatRoom,
                userId = userId
            )
        }
    }

    fun updateLastReadMessageId(messageId: Long) {
        this.lastReadMessageId = messageId
    }
}
