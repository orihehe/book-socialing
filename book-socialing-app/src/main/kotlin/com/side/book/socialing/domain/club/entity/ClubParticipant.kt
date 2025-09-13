package com.side.book.socialing.domain.club.entity

import com.side.book.socialing.domain.club.enum.ParticipantRole
import com.side.book.socialing.domain.club.enum.ParticipantStatus
import com.side.book.socialing.domain.common.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "club_participant")
class ClubParticipant (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id", nullable = false)
    var club: Club,

    @Column(name = "user_id", nullable = false)
    var userId: Long, // TODO: User 엔티티 구현 후 @ManyToOne으로 변경 고려

    @Enumerated(EnumType.STRING)
    @Column(name = "role", length = 20, nullable = false)
    var role: ParticipantRole,

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    var status: ParticipantStatus
): BaseEntity() {
    companion object {
        fun create(club: Club, userId: Long, role: ParticipantRole, status: ParticipantStatus): ClubParticipant {
            return ClubParticipant(
                club = club,
                userId = userId,
                role = role,
                status = status
            )
        }
    }
}
