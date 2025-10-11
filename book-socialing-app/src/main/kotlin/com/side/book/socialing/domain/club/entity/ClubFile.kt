package com.side.book.socialing.domain.club.entity

import com.side.book.socialing.domain.common.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.hibernate.annotations.SQLRestriction

@Entity
@Table(name = "club_file")
@SQLRestriction("deleted = false")
class ClubFile(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id", nullable = false)
    var club: Club,

    @Column(name = "original_file_name", nullable = false)
    var originalFileName: String,

    @Column(name = "stored_file_name", nullable = false, unique = true)
    var storedFileName: String,

    @Column(name = "file_path", length = 500, nullable = false)
    var filePath: String,

    @Column(name = "file_size", nullable = false)
    var fileSize: Long,

    @Column(name = "deleted", nullable = false)
    var deleted: Boolean = false

) : BaseEntity() {
    companion object {
        fun create(club: Club, originalFileName: String, storedFileName: String, filePath: String, fileSize: Long): ClubFile {
            return ClubFile(
                club = club,
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
