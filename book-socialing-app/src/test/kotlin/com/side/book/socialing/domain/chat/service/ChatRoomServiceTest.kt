package com.side.book.socialing.domain.chat.service

import com.side.book.socialing.domain.chat.entity.ChatRoom
import com.side.book.socialing.domain.chat.entity.ChatRoomParticipant
import com.side.book.socialing.domain.chat.repository.ChatRoomParticipantRepository
import com.side.book.socialing.domain.chat.repository.ChatRoomRepository
import com.side.book.socialing.global.error.exception.ResourceNotFoundException
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.data.repository.findByIdOrNull

class ChatRoomServiceTest {
    private lateinit var chatRoomService: ChatRoomService
    private lateinit var chatRoomRepository: ChatRoomRepository
    private lateinit var chatRoomParticipantRepository: ChatRoomParticipantRepository

    @BeforeEach
    fun setup() {
        chatRoomRepository = mockk()
        chatRoomParticipantRepository = mockk()
        chatRoomService = ChatRoomService(chatRoomRepository, chatRoomParticipantRepository)
    }

    @Nested
    @DisplayName("createChatRoomForNote")
    inner class CreateChatRoomForNote {
        @Test
        @DisplayName("정상적으로 채팅방 생성을 요청하면 noteId와 roomName을 가진 ChatRoom이 저장된다")
        fun createChatRoom() {
            // Given
            val noteId = 1L
            val roomName = "Test Chat Room"
            val chatRoomSlot = slot<ChatRoom>()
            every { chatRoomRepository.save(capture(chatRoomSlot)) } returns mockk()

            // When
            chatRoomService.createChatRoomForNote(noteId, roomName)

            // Then
            verify(exactly = 1) { chatRoomRepository.save(any()) }
            val capturedChatRoom = chatRoomSlot.captured
            assertEquals(noteId, capturedChatRoom.noteId)
            assertEquals(roomName, capturedChatRoom.roomName)
        }
    }

    @Nested
    @DisplayName("joinRoom")
    inner class JoinRoom {
        private val roomId = 1L
        private val userId = 1L

        @Test
        @DisplayName("존재하지 않는 채팅방에 참여하려고 하면 ResourceNotFoundException이 발생한다")
        fun roomNotFound() {
            // Given
            every { chatRoomRepository.findByIdOrNull(roomId) } returns null

            // When & Then
            assertThrows<ResourceNotFoundException> {
                chatRoomService.joinRoom(roomId, userId)
            }
        }

        @Test
        @DisplayName("이미 참여한 유저가 참여하려고 하면 IllegalStateException이 발생한다")
        fun alreadyJoined() {
            // Given
            val chatRoom = mockk<ChatRoom>()
            every { chatRoom.id } returns roomId
            every { chatRoomRepository.findByIdOrNull(roomId) } returns chatRoom
            every { chatRoomParticipantRepository.findByChatRoomIdAndUserId(roomId, userId) } returns mockk()

            // When & Then
            assertThrows<IllegalStateException> {
                chatRoomService.joinRoom(roomId, userId)
            }
        }

        @Test
        @DisplayName("정상적으로 채팅방 참여를 요청하면 ChatRoomParticipant가 저장된다")
        fun joinRoom() {
            // Given
            val chatRoom = mockk<ChatRoom>()
            val participantSlot = slot<ChatRoomParticipant>()
            every { chatRoom.id } returns roomId
            every { chatRoomRepository.findByIdOrNull(roomId) } returns chatRoom
            every { chatRoomParticipantRepository.findByChatRoomIdAndUserId(roomId, userId) } returns null
            every { chatRoomParticipantRepository.save(capture(participantSlot)) } returns mockk()

            // When
            chatRoomService.joinRoom(roomId, userId)

            // Then
            verify(exactly = 1) { chatRoomParticipantRepository.save(any()) }
            val capturedParticipant = participantSlot.captured
            assertEquals(chatRoom, capturedParticipant.chatRoom)
            assertEquals(userId, capturedParticipant.userId)
        }
    }
}
