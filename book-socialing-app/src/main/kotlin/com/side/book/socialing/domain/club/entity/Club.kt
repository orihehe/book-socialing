package com.side.book.socialing.domain.club.entity

import com.side.book.socialing.domain.club.command.CreateClubCommand
import com.side.book.socialing.domain.common.BaseEntity
import com.side.book.socialing.domain.note.entity.ClubReview
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import org.hibernate.annotations.BatchSize

@Entity
@Table(name = "club")
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
    var participants: MutableList<ClubParticipant> = mutableListOf(),

    @BatchSize(size = 100)
    @OneToMany(mappedBy = "club")
    var files: MutableList<ClubFile> = mutableListOf(),

    @BatchSize(size = 100)
    @OneToMany(mappedBy = "club")
    var reviews: MutableList<ClubReview> = mutableListOf()
) : BaseEntity() {
    companion object {
        fun create(cmd: CreateClubCommand): Club {
            return Club(
                clubName = cmd.clubName,
                description = cmd.description
            )
        }
    }
}
