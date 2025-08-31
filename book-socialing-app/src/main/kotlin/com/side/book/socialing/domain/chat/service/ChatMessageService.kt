package com.side.book.socialing.domain.chat.service

import com.side.book.socialing.domain.chat.command.SaveMessageCommand
import com.side.book.socialing.domain.chat.dto.ChatMessagesResponse
import com.side.book.socialing.domain.chat.dto.SavedMessageDto
import com.side.book.socialing.domain.chat.entity.ChatMessage
import com.side.book.socialing.domain.chat.entity.MessageType
import com.side.book.socialing.domain.chat.repository.ChatMessageRepository
import com.side.book.socialing.domain.chat.repository.ChatRoomParticipantRepository
import com.side.book.socialing.domain.chat.repository.ChatRoomRepository
import com.side.book.socialing.global.error.exception.ResourceNotFoundException
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ChatMessageService(
    private val chatMessageRepository: ChatMessageRepository,
    private val chatRoomRepository: ChatRoomRepository,
    private val chatRoomParticipantRepository: ChatRoomParticipantRepository
) {

    @Transactional
    fun saveMessage(command: SaveMessageCommand): SavedMessageDto {
        // TODO: 추후 User 모듈을 추가하여 실제 사용자 닉네임을 조회해야함
        val senderNickname = "임시닉네임(ID: ${command.senderId})"

        val chatRoom = chatRoomRepository.findByIdOrNull(command.roomId)
            ?: throw ResourceNotFoundException("ChatRoom not found")
        chatRoomParticipantRepository.findByChatRoomIdAndUserId(command.roomId, command.senderId)
            ?: throw IllegalArgumentException("User ${command.senderId} not participate chat room ${command.roomId}")

        val message = ChatMessage.create(
            chatRoom = chatRoom,
            senderId = command.senderId,
            content = command.content,
            messageType = MessageType.valueOf(command.messageType)
        )
        val savedMessage = chatMessageRepository.save(message)

        return SavedMessageDto(
            messageId = savedMessage.id!!,
            senderNickname = senderNickname,
            content = savedMessage.content,
            messageType = savedMessage.messageType.name,
            sentAt = savedMessage.createdAt
        )
    }

    @Transactional(readOnly = true)
    fun findMessagesByRoomId(roomId: Long, userId: Long, lastMessageId: Long?, pageSize: Int): ChatMessagesResponse {
        if (!chatRoomRepository.existsById(roomId)) {
            throw ResourceNotFoundException("ChatRoom not found")
        }

        val cursor = lastMessageId ?: chatRoomParticipantRepository.findByChatRoomIdAndUserId(roomId, userId)?.lastReadMessageId ?: Long.MAX_VALUE
        val pageable = PageRequest.of(0, pageSize)

        val messagesSlice = chatMessageRepository.findByChatRoomIdAndIdLessThanOrderByIdDesc(roomId, cursor, pageable)

        val savedMessageDtos = messagesSlice.content.map {
            SavedMessageDto(
                messageId = it.id!!,
                senderNickname = "임시닉네임(ID: ${it.senderId})",
                content = it.content,
                messageType = it.messageType.name,
                sentAt = it.createdAt
            )
        }

        return ChatMessagesResponse(
            messages = savedMessageDtos,
            hasNext = messagesSlice.hasNext()
        )
    }
}
