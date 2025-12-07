package com.side.book.socialing.domain.club.listener

import com.side.book.socialing.domain.club.repository.ClubParticipantRepository
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
class ClubEventListener(
    private val clubParticipantRepository: ClubParticipantRepository
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
        log.info("Handling user withdrawn event for clubs: {}", event)
        val participants = clubParticipantRepository.findAllByUserId(event.userId)

        participants.forEach { participant ->
            if (participant.isHost()) {
                log.info("System deleting club {} as host {} is withdrawn", participant.club.id, event.userId)
                // TODO: pass club to other user
                participant.club.systemDelete()
            }
            log.info("Deleting club participant {} for user {}", participant.id, event.userId)
            participant.delete()
        }
    }

    @Recover
    fun recover(exception: Exception, event: UserWithdrawnEvent) {
        log.error(
            "Failed to process UserWithdrawnEvent for clubs after multiple retries for user {}. Reason: {}",
            event.userId,
            exception.message
        )
    }
}
