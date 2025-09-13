package com.side.book.socialing.domain.meeting.entity

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@Entity
@EntityListeners(AuditingEntityListener::class)
@Table(name = "meeting_participant")
class MeetingParticipant(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_id", nullable = false)
    var meeting: Meeting,

    @Column(name = "user_id", nullable = false)
    var userId: Long,

    @Column(name = "joined_at", nullable = false)
    var joinedAt: LocalDateTime = LocalDateTime.now(),

    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false)
    var createdAt: LocalDateTime = LocalDateTime.now(),

    @CreatedBy
    @Column(name = "created_by", updatable = false)
    var createdBy: String? = null,

    @LastModifiedDate
    @Column(name = "modified_at", nullable = false)
    var modifiedAt: LocalDateTime = LocalDateTime.now(),

    @LastModifiedBy
    @Column(name = "modified_by")
    var modifiedBy: String? = null
)
