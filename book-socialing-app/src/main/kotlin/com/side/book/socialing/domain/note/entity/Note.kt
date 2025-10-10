package com.side.book.socialing.domain.note.entity

import com.side.book.socialing.domain.club.entity.Club
import com.side.book.socialing.domain.common.BaseEntity
import com.side.book.socialing.domain.note.command.CreateNoteCommand
import com.side.book.socialing.global.error.exception.ForbiddenException
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import org.hibernate.annotations.BatchSize
import org.hibernate.annotations.SQLRestriction
import java.time.LocalDateTime

@Entity
@Table(name = "note")
@SQLRestriction("deleted = false")
class Note(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id", nullable = true)
    var club: Club? = null,

    @Column(name = "book_name", length = 200)
    var bookName: String,

    @Column(name = "book_author", length = 100)
    var bookAuthor: String,

    @Column(name = "description", length = 1000)
    var description: String? = null,

    @Column(name = "start_at")
    var startAt: LocalDateTime,

    @Column(name = "end_at")
    var endAt: LocalDateTime,

    @Column(name = "deleted", nullable = false)
    var deleted: Boolean = false,

    @BatchSize(size = 100)
    @OneToMany(mappedBy = "note")
    var participants: MutableList<NoteParticipant> = mutableListOf(),

    @BatchSize(size = 100)
    @OneToMany(mappedBy = "note")
    var files: MutableList<NoteFile> = mutableListOf()
) : BaseEntity() {
    companion object {
        fun create(cmd: CreateNoteCommand, club: Club?): Note {
            return Note(
                club = club,
                bookName = cmd.bookName,
                bookAuthor = cmd.bookAuthor,
                description = cmd.description,
                startAt = cmd.startAt,
                endAt = cmd.endAt
            )
        }
    }

    fun isHost(userId: Long): Boolean {
        return this.participants.any { it.userId == userId && it.isHost() }
    }

    fun delete(userId: Long) {
        if (!isHost(userId)) {
            throw ForbiddenException("User $userId doesn't have permission to delete this note.")
        }
        deleted = true
    }
}
