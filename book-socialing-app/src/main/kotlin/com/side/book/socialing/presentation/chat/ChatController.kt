package com.side.book.socialing.presentation.chat

import com.side.book.socialing.domain.chat.command.SaveMessageCommand
import com.side.book.socialing.domain.chat.service.ChatMessageService
import com.side.book.socialing.global.auth.UserPrincipalResolver
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

@Tag(name = "채팅 API", description = "채팅 관련 API")
@RestController
@RequestMapping("/api/chat/v1")
class ChatController(
    private val chatMessageService: ChatMessageService,
    private val userPrincipalResolver: UserPrincipalResolver
) {

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    fun sendMessage(
        request: ChatMessageRequest
    ): ChatMessageResponse {
        val userId = userPrincipalResolver.getUserId()

        val command = SaveMessageCommand(
            roomId = request.roomId,
            senderId = userId,
            content = request.content,
            messageType = request.type
        )

        val savedMessageDto = chatMessageService.saveMessage(command)

        return ChatMessageResponse(
            messageId = savedMessageDto.messageId,
            userId = savedMessageDto.userId,
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
        @RequestParam(required = false) messageType: String,
        @RequestParam(required = false) lastMessageId: Long?,
        @RequestParam(defaultValue = "20") size: Int
    ): ChatMessagesApiResponse {
        val userId = userPrincipalResolver.getUserId()
        val queryResult = chatMessageService.findMessagesByRoomId(roomId, userId, messageType, lastMessageId, size)

        val messages = queryResult.messages.map {
            ChatMessageResponse(
                messageId = it.messageId,
                userId = it.userId,
                content = it.content,
                type = it.messageType,
                sentAt = it.sentAt
            )
        }

        return ChatMessagesApiResponse(
            messages = messages,
            hasNext = queryResult.hasNext
        )
    }
}
