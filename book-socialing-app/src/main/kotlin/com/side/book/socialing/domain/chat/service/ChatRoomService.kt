package com.side.book.socialing.domain.chat.service

import com.side.book.socialing.domain.chat.entity.ChatRoom
import com.side.book.socialing.domain.chat.entity.ChatRoomParticipant
import com.side.book.socialing.domain.chat.repository.ChatRoomParticipantRepository
import com.side.book.socialing.domain.chat.repository.ChatRoomRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ChatRoomService(
    private val chatRoomRepository: ChatRoomRepository,
    private val chatRoomParticipantRepository: ChatRoomParticipantRepository
) {

    fun createChatRoom(roomName: String) {
        val chatRoom = ChatRoom.create(roomName)
        chatRoomRepository.save(chatRoom)
    }

    @Transactional
    fun joinRoom(roomId: Long, userId: Long) {
            ?: throw ResourceNotFoundException("Not Found ChatRoom")
            ?: throw IllegalArgumentException("Not Found ChatRoom")

        chatRoomParticipantRepository.findByChatRoomIdAndUserId(chatRoom.id!!, userId)?.let {
            throw IllegalStateException("Already participate user")
        }

        val participant = ChatRoomParticipant.create(chatRoom, userId)
        chatRoomParticipantRepository.save(participant)
    }
}
