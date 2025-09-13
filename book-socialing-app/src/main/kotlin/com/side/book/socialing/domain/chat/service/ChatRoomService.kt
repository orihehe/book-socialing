package com.side.book.socialing.domain.chat.service

import com.side.book.socialing.domain.chat.entity.ChatRoom
import com.side.book.socialing.domain.chat.entity.ChatRoomParticipant
import com.side.book.socialing.domain.chat.repository.ChatRoomParticipantRepository
import com.side.book.socialing.domain.chat.repository.ChatRoomRepository
import com.side.book.socialing.global.error.exception.ResourceNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ChatRoomService(
    private val chatRoomRepository: ChatRoomRepository,
    private val chatRoomParticipantRepository: ChatRoomParticipantRepository
) {
    @Transactional
    fun createChatRoomForNote(
        noteId: Long,
        roomName: String
    ) {
        val chatRoom = ChatRoom.create(
            roomName = roomName,
            noteId = noteId
        )
        chatRoomRepository.save(chatRoom)
    }

    @Transactional
    fun joinRoom(
        noteId: Long,
        userId: Long
    ) {
        val chatRoom = chatRoomRepository.findByNoteId(noteId)
            ?: throw ResourceNotFoundException("Not Found ChatRoom")

        chatRoomParticipantRepository.findByChatRoomIdAndUserId(chatRoom.id!!, userId)?.let {
            throw IllegalStateException("Already participate user")
        }

        val participant = ChatRoomParticipant.create(chatRoom, userId)
        chatRoomParticipantRepository.save(participant)
    }
}
