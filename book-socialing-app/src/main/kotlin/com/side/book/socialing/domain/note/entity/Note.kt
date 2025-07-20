package com.side.book.socialing.domain.note.entity

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
@Table(name = "note")
class Note(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

// TODO: club 구현 후
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "club_id", nullable = false)
//    var club: Club,

    @Column(name = "description", length = 1000)
    var description: String? = null,

    @Column(name = "book_name", length = 200)
    var bookName: String? = null,

    @Column(name = "book_author", length = 100)
    var bookAuthor: String? = null,

    @Column(name = "start_date")
    var startDate: LocalDateTime,

    @Column(name = "end_date")
    var endDate: LocalDateTime,

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

    // Join Fetch를 위해 설정된 부분
    @OneToMany(mappedBy = "note")
    var participants: MutableList<NoteParticipant> = mutableListOf(),

    @OneToMany(mappedBy = "note")
    var files: MutableList<NoteFile> = mutableListOf()
) {
}
