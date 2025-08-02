package com.side.book.socialing.presentation.chat

import com.side.book.socialing.presentation.chat.dto.ChatMessageRequest
import com.side.book.socialing.presentation.chat.dto.ChatMessageResponse
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.stereotype.Controller
import java.security.Principal
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Controller
class ChatController(
    // private val chatService: ChatService // TODO: 도메인 서비스 계층 주입
) {

    /**
     * /app/chat.sendMessage 경로로 메시지가 오면 이 메서드가 처리합니다.
     */
    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    fun sendMessage(
        request: ChatMessageRequest,
        principal: Principal
    ): ChatMessageResponse {

        // principal.name은 보통 사용자의 username (ID) 입니다.
        val username = principal.name

        // TODO: username으로 DB에서 유저의 닉네임 등 상세 정보 조회
        // val user = userService.findByUsername(username)
        // val senderNickname = user.nickname
        val senderNickname = "임시닉네임($username)" // 임시로 닉네임 설정

        // TODO: 받은 메시지를 ChatService를 통해 DB에 저장하는 로직
        // val savedMessage = chatService.save(request, user)

        // 응답 DTO 생성
        return ChatMessageResponse(
            messageId = System.currentTimeMillis(), // TODO: 임시 ID
            senderNickname = senderNickname,
            content = request.content,
            type = request.type,
            emojis = request.emojis,
            sentAt = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        )
    }
}
