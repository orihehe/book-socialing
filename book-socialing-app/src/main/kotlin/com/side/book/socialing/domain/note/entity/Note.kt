package com.side.book.socialing.domain.note.entity

import com.side.book.socialing.domain.common.BaseEntity
import com.side.book.socialing.domain.note.command.CreateNoteCommand
import jakarta.persistence.*
import org.hibernate.annotations.BatchSize
import java.time.LocalDateTime

@Entity
@Table(name = "note")
class Note(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

// TODO: club 구현 후
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "club_id", nullable = false)
//    var club: Club,

    @Column(name = "book_name", length = 200)
    var bookName: String,

    @Column(name = "book_author", length = 100)
    var bookAuthor: String,

    @Column(name = "description", length = 1000)
    var description: String? = null,

    @Column(name = "start_date")
    var startDate: LocalDateTime,

    @Column(name = "end_date")
    var endDate: LocalDateTime,

    @BatchSize(size = 100)
    @OneToMany(mappedBy = "note")
    var participants: MutableList<NoteParticipant> = mutableListOf(),

    @BatchSize(size = 100)
    @OneToMany(mappedBy = "note")
    var files: MutableList<NoteFile> = mutableListOf()
): BaseEntity() {
    companion object {
        fun create(cmd: CreateNoteCommand): Note {
            return Note(
                bookName = cmd.bookName,
                bookAuthor = cmd.bookAuthor,
                description = cmd.description,
                startDate = cmd.startDate,
                endDate = cmd.endDate
            )
        }
    }
}
