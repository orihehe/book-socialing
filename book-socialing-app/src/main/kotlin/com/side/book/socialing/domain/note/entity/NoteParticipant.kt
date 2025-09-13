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
        fun create(note: Note, userId: Long, role: ParticipantRole, status: ParticipantStatus): NoteParticipant {
            return NoteParticipant(
                note = note,
                userId = userId,
                role = role,
                status = status
            )
        }
    }
}
