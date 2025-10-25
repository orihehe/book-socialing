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
        val chatRoom = chatRoomRepository.findByNoteId(command.noteId)
            ?: throw ResourceNotFoundException("ChatRoom not found")
        chatRoomParticipantRepository.findByChatRoomIdAndUserId(chatRoom.id!!, command.senderId)
            ?: throw IllegalArgumentException("User ${command.senderId} not participate chat room ${command.noteId}")

        val message = ChatMessage.create(
            chatRoom = chatRoom,
            senderId = command.senderId,
            content = command.content,
            messageType = MessageType.valueOf(command.messageType)
        )
        val savedMessage = chatMessageRepository.save(message)

        return SavedMessageDto(
            messageId = savedMessage.id!!,
            userId = command.senderId,
            content = savedMessage.content,
            messageType = savedMessage.messageType.name,
            sentAt = savedMessage.createdAt
        )
    }

    @Transactional(readOnly = true)
    fun findMessages(
        noteId: Long,
        userId: Long,
        messageType: String?,
        lastMessageId: Long?,
        pageSize: Int
    ): ChatMessagesResponse {
        if (!chatRoomRepository.existsById(noteId)) {
            throw ResourceNotFoundException("ChatRoom not found")
        }

        val cursor = lastMessageId
            ?: chatRoomParticipantRepository.findByChatRoomIdAndUserId(noteId, userId)?.lastReadMessageId
            ?: Long.MAX_VALUE
        val pageable = PageRequest.of(0, pageSize)

        val messagesSlice = if (messageType != null) {
            val messageTypeEnum = MessageType.valueOf(messageType)
            chatMessageRepository.findByChatRoomIdAndMessageTypeAndIdLessThanOrderByIdDesc(noteId, messageTypeEnum, cursor, pageable)
        } else {
            chatMessageRepository.findByChatRoomIdAndIdLessThanOrderByIdDesc(noteId, cursor, pageable)
        }

        val savedMessageDtos = messagesSlice.content.map {
            SavedMessageDto(
                messageId = it.id!!,
                userId = it.senderId,
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
