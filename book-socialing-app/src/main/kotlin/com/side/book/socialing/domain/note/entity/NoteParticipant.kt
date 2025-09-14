package com.side.book.socialing.domain.note.entity

import com.side.book.socialing.domain.common.BaseEntity
import com.side.book.socialing.domain.note.enum.ParticipantRole
import com.side.book.socialing.domain.note.enum.ParticipantStatus
import jakarta.persistence.*

@Entity
@Table(name = "note_participant")
class NoteParticipant(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "note_id", nullable = false)
    var note: Note,

    @Column(name = "user_id", nullable = false)
    var userId: Long,

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 20)
    var role: ParticipantRole = ParticipantRole.MEMBER,

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    var status: ParticipantStatus = ParticipantStatus.JOINED
) : BaseEntity() {
    companion object {

        fun createHost(note: Note, userId: Long): NoteParticipant {
            return NoteParticipant(
                note = note,
                userId = userId,
                role = ParticipantRole.HOST,
                status = ParticipantStatus.JOINED
            )
        }

        fun createMember(note: Note, userId: Long): NoteParticipant {
            return NoteParticipant(
                note = note,
                userId = userId,
                role = ParticipantRole.MEMBER,
                status = ParticipantStatus.PENDING_APPROVAL
            )
        }
    }

    fun cancel() {
        if (!isWaitingApproval()) {
            throw IllegalStateException("Only pending approval participant can be canceled. Current status: ${this.status}")
        }
        this.status = ParticipantStatus.CANCEL
    }

    fun approve() {
        if (!isWaitingApproval()) {
            throw IllegalStateException("Only pending approval participant can be approved. Current status: ${this.status}")
        }
        this.status = ParticipantStatus.JOINED
    }

    fun reject() {
        if (!isWaitingApproval()) {
            throw IllegalStateException("Only pending approval participant can be rejected. Current status: ${this.status}")
        }
        this.status = ParticipantStatus.REJECTED
    }

    fun joinRequest() {
        if (!canRequestJoin()) {
            throw IllegalStateException("User $userId can't request join. Current status: ${this.status}")
        }
        this.status = ParticipantStatus.PENDING_APPROVAL
    }

    fun kick() {
        if (!isJoined()) {
            throw IllegalStateException("Only joined participant can be kicked. Current status: ${this.status}")
        }
        this.status = ParticipantStatus.KICKED
    }

    fun left() {
        if (!isJoined()) {
            throw IllegalStateException("Only joined participant can be left. Current status: ${this.status}")
        }
        this.status = ParticipantStatus.LEFT
    }

    fun isHost(): Boolean {
        return this.role == ParticipantRole.HOST
    }

    private fun canRequestJoin(): Boolean {
        // TODO: consider REJECTED / KICKED status
        return this.status != ParticipantStatus.PENDING_APPROVAL &&
            this.status != ParticipantStatus.JOINED
    }

    private fun isWaitingApproval(): Boolean {
        return this.status == ParticipantStatus.PENDING_APPROVAL
    }

    private fun isJoined(): Boolean {
        return this.status == ParticipantStatus.JOINED
    }
}
