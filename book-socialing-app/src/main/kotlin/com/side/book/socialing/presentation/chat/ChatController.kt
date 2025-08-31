package com.side.book.socialing.presentation.chat

import com.side.book.socialing.domain.chat.command.SaveMessageCommand
import com.side.book.socialing.domain.chat.service.ChatMessageService
import com.side.book.socialing.presentation.chat.dto.ChatMessageRequest
import com.side.book.socialing.presentation.chat.dto.ChatMessageResponse
import com.side.book.socialing.presentation.chat.dto.ChatMessagesApiResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@Tag(name = "채팅 API", description = "채팅 관련 API")
@RestController
@RequestMapping("/api/chat/v1")
class ChatController(
    private val chatMessageService: ChatMessageService
) {

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    fun sendMessage(
        request: ChatMessageRequest,
        principal: Principal
    ): ChatMessageResponse {
        // TODO: 추후 인증 모듈에서 실제 userId를 가져와야 함
        val userId = 1L

        val command = SaveMessageCommand(
            roomId = request.roomId,
            senderId = userId,
            content = request.content,
            messageType = request.type
        )

        val savedMessageDto = chatMessageService.saveMessage(command)

        return ChatMessageResponse(
            messageId = savedMessageDto.messageId,
            senderNickname = savedMessageDto.senderNickname,
            content = savedMessageDto.content,
            type = savedMessageDto.messageType,
            sentAt = savedMessageDto.sentAt
        )
    }

    @Operation(
        summary = "채팅방의 이전 대화 내역 조회",
        description = "특정 채팅방에 입장했을 때, 이전에 나눈 대화 내역을 모두 조회합니다."
    )
    @GetMapping("/rooms/{roomId}/messages")
    fun getChatMessages(
        @PathVariable roomId: Long,
        @RequestParam(required = false) lastMessageId: Long?,
        @RequestParam(defaultValue = "20") size: Int,
        principal: Principal
    ): ChatMessagesApiResponse {
        // TODO: 추후 인증 모듈에서 실제 userId를 가져와야 함
        val userId = 1L
        val chatMessagesResponse = chatMessageService.findMessagesByRoomId(roomId, userId, lastMessageId, size)

        val messages = chatMessagesResponse.messages.map {
            ChatMessageResponse(
                messageId = it.messageId,
                senderNickname = it.senderNickname,
                content = it.content,
                type = it.messageType,
                sentAt = it.sentAt
            )
        }

        return ChatMessagesApiResponse(
            messages = messages,
            hasNext = chatMessagesResponse.hasNext
        )
    }
}
