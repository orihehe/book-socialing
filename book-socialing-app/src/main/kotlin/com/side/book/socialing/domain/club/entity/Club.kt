package com.side.book.socialing.domain.club.entity

import com.side.book.socialing.domain.club.command.CreateClubCommand
import com.side.book.socialing.domain.common.BaseEntity
import com.side.book.socialing.domain.note.entity.ClubReview
import com.side.book.socialing.global.error.exception.ForbiddenException
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import org.hibernate.annotations.BatchSize
import org.hibernate.annotations.SQLRestriction

@Entity
@Table(name = "club")
@SQLRestriction("deleted = false")
class Club(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "club_name", length = 200, nullable = false)
    var clubName: String,

    @Column(name = "description", length = 1000)
    var description: String? = null,

    @BatchSize(size = 100)
    @OneToMany(mappedBy = "club")
    var participants: List<ClubParticipant> = listOf(),

    @BatchSize(size = 100)
    @OneToMany(mappedBy = "club")
    var files: List<ClubFile> = listOf(),

    @BatchSize(size = 100)
    @OneToMany(mappedBy = "club")
    var reviews: List<ClubReview> = listOf(),

    @Column(name = "deleted", nullable = false)
    var deleted: Boolean = false

) : BaseEntity() {
    companion object {
        fun create(cmd: CreateClubCommand): Club {
            return Club(
                clubName = cmd.clubName,
                description = cmd.description
            )
        }
    }

    fun delete(userId: Long) {
        if (!isHost(userId)) {
            throw ForbiddenException("User $userId doesn't have permission to delete this note.")
        }
        deleted = true
    }

    fun isHost(userId: Long): Boolean {
        return this.participants.any { it.userId == userId && it.isHost() }
    }
}
