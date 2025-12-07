package com.side.book.socialing.domain.note.listener

import com.side.book.socialing.domain.note.repository.NoteParticipantRepository
import com.side.book.socialing.domain.user.event.UserWithdrawnEvent
import com.side.book.socialing.global.utils.log
import org.springframework.dao.OptimisticLockingFailureException
import org.springframework.dao.PessimisticLockingFailureException
import org.springframework.retry.annotation.Backoff
import org.springframework.retry.annotation.Recover
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.event.TransactionalEventListener

@Component
class NoteEventListener(
    private val noteParticipantRepository: NoteParticipantRepository
) {

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Retryable(
        value = [
            OptimisticLockingFailureException::class,
            PessimisticLockingFailureException::class
        ],
        maxAttempts = 3,
        backoff = Backoff(delay = 1000, multiplier = 2.0)
    )
    @TransactionalEventListener
    fun handleUserWithdrawnEvent(event: UserWithdrawnEvent) {
        log.info("Handling user withdrawn event for notes: {}", event)
        val participants = noteParticipantRepository.findAllByUserId(event.userId)

        participants.forEach { participant ->
            if (participant.isHost()) {
                log.info("System deleting note {} as host {} is withdrawn", participant.note.id, event.userId)
                // TODO: pass note to other user
                participant.note.systemDelete()
            }
            log.info("Deleting note participant {} for user {}", participant.id, event.userId)
            participant.delete()
        }
    }

    @Recover
    fun recover(exception: Exception, event: UserWithdrawnEvent) {
        log.error(
            "Failed to process UserWithdrawnEvent for notes after multiple retries for user {}. Reason: {}",
            event.userId,
            exception.message
        )
    }
}
