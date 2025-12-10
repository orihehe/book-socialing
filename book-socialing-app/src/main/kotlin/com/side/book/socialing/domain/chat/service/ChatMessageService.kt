package com.side.book.socialing.domain.chat.service

import com.side.book.socialing.domain.chat.command.SaveMessageCommand
import com.side.book.socialing.domain.chat.dto.ChatMessagesResponse
import com.side.book.socialing.domain.chat.dto.SavedMessageDto
import com.side.book.socialing.domain.chat.entity.ChatMessage
import com.side.book.socialing.domain.chat.entity.ChatRoomParticipant
import com.side.book.socialing.domain.chat.entity.MessageType
import com.side.book.socialing.domain.chat.repository.ChatMessageRepository
import com.side.book.socialing.domain.chat.repository.ChatRoomParticipantRepository
import com.side.book.socialing.domain.chat.repository.ChatRoomRepository
import com.side.book.socialing.domain.club.enum.ParticipantStatus
import com.side.book.socialing.domain.club.repository.ClubParticipantRepository
import com.side.book.socialing.domain.note.repository.NoteRepository
import com.side.book.socialing.global.error.exception.ForbiddenException
import com.side.book.socialing.global.error.exception.ResourceNotFoundException
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ChatMessageService(
    private val chatMessageRepository: ChatMessageRepository,
    private val chatRoomRepository: ChatRoomRepository,
    private val chatRoomParticipantRepository: ChatRoomParticipantRepository,
    private val noteRepository: NoteRepository,
    private val clubParticipantRepository: ClubParticipantRepository
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

    @Transactional
    fun findMessages(
        noteId: Long,
        userId: Long,
        messageType: String?,
        lastMessageId: Long?,
        pageSize: Int
    ): ChatMessagesResponse {
        val chatRoom = chatRoomRepository.findByNoteId(noteId)
            ?: throw ResourceNotFoundException("ChatRoom not found")

        // TODO: refactor this
        var chatRoomParticipant = chatRoomParticipantRepository.findByChatRoomIdAndUserId(chatRoom.id!!, userId)

        if (chatRoomParticipant == null) {
            val note = noteRepository.findByIdAndDeletedFalse(noteId)
                ?: throw ResourceNotFoundException("Note not found for chat room $noteId")

            val club = note.club
            if (club != null) {
                val clubParticipant = clubParticipantRepository.findByClubIdAndUserId(club.id!!, userId)
                if (clubParticipant != null && clubParticipant.status == ParticipantStatus.JOINED) {
                    // User is a joined member of the club, add them as a chat room participant
                    chatRoomParticipant = chatRoomParticipantRepository.save(ChatRoomParticipant.create(chatRoom, userId))
                }
            }
        }

        chatRoomParticipant ?: throw ForbiddenException("User $userId not participate chat room $noteId")
        val roomId = chatRoom.id

        val cursor = lastMessageId
            ?: chatRoomParticipantRepository.findByChatRoomIdAndUserId(roomId, userId)?.lastReadMessageId
            ?: Long.MAX_VALUE
        val pageable = PageRequest.of(0, pageSize)

        val messagesSlice = if (messageType != null) {
            val messageTypeEnum = MessageType.valueOf(messageType)
            chatMessageRepository.findByChatRoomIdAndMessageTypeAndIdLessThanOrderByIdDesc(roomId, messageTypeEnum, cursor, pageable)
        } else {
            chatMessageRepository.findByChatRoomIdAndIdLessThanOrderByIdDesc(roomId, cursor, pageable)
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
