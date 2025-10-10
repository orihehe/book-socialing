package com.side.book.socialing.domain.note.entity

import com.side.book.socialing.domain.common.BaseEntity
import jakarta.persistence.*
import org.hibernate.annotations.SQLRestriction

@Entity
@Table(name = "note_file")
@SQLRestriction("deleted = false")
class NoteFile(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "note_id", nullable = false)
    var note: Note,

    @Column(name = "original_file_name", nullable = false, length = 255)
    var originalFileName: String,

    @Column(name = "stored_file_name", nullable = false, unique = true, length = 255)
    var storedFileName: String,

    @Column(name = "file_path", nullable = false, length = 500)
    var filePath: String,

    @Column(name = "file_size", nullable = false)
    var fileSize: Long,

    @Column(name = "deleted", nullable = true)
    var deleted: Boolean = false

) : BaseEntity() {
    companion object {
        fun create(note: Note, originalFileName: String, storedFileName: String, filePath: String, fileSize: Long): NoteFile {
            return NoteFile(
                note = note,
                originalFileName = originalFileName,
                storedFileName = storedFileName,
                filePath = filePath,
                fileSize = fileSize
            )
        }
    }

    fun delete() {
        deleted = true
    }
}
