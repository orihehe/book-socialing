package com.side.book.socialing.domain.meeting.entity

import com.side.book.socialing.domain.meeting.command.CreateMeetingCommand
import jakarta.persistence.*
import jakarta.persistence.Entity
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@Entity
@EntityListeners(AuditingEntityListener::class)
@Table(name = "meeting")
class Meeting(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "name", length = 100, nullable = false)
    var name: String,

    @Column(name = "description", length = 1000)
    var description: String? = null,

    @Column(name = "round")
    var round: Int,

    @Column(name = "book_name", length = 200)
    var bookName: String? = null,

    @Column(name = "book_author", length = 100)
    var bookAuthor: String? = null,

    @Column(name = "book_link", length = 200)
    var bookLink: String? = null,

    @Column(name = "meet_date")
    var meetDate: LocalDateTime,

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    var createdAt: LocalDateTime? = null,

    @CreatedBy
    @Column(name = "created_by", updatable = false)
    var createdBy: String? = null,

    @LastModifiedDate
    @Column(name = "modified_at", nullable = false)
    var modifiedAt: LocalDateTime? = null,

    @LastModifiedBy
    @Column(name = "modified_by")
    var modifiedBy: String? = null,
) {
    companion object {
        fun create(cmd: CreateMeetingCommand): Meeting {
            return Meeting(
                name = cmd.name,
                description = cmd.description,
                bookName = cmd.bookName,
                bookAuthor = cmd.bookAuthor,
                bookLink = cmd.bookLink,
                meetDate = cmd.meetDate,
                round = cmd.round,
                createdBy = cmd.createdBy,
                modifiedBy = cmd.createdBy,
            )
        }
    }
}
