package com.side.book.socialing.domain.chat.service

import com.side.book.socialing.domain.note.event.NoteCreatedEvent
import com.side.book.socialing.global.utils.log
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class ChatNoteEventListener(
    private val chatRoomService: ChatRoomService
) {
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun handleNoteCreatedEvent(event: NoteCreatedEvent) {
        try {
            chatRoomService.createChatRoomForNote(
                noteId = event.noteId,
                roomName = event.noteTitle
            )
        } catch (e: Exception) {
            log.error("Failed to create chat room for noteId: {}.", event.noteId, e)
        }
    }
}
