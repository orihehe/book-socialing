package com.side.book.socialing.domain.chat.entity

import com.side.book.socialing.domain.common.BaseEntity
import jakarta.persistence.*

@Entity
@Table(name = "chat_rooms")
class ChatRoom(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    val roomName: String,

    @Column(nullable = false)
    var state: ChatRoomState

) : BaseEntity() {

    companion object {
        fun create(roomName: String): ChatRoom {
            return ChatRoom(
                roomName = roomName,
                state = ChatRoomState.OPENED
            )
        }
    }

    fun close() {
        state = ChatRoomState.CLOSED
    }
}
