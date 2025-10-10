package com.side.book.socialing.domain.note.entity

import com.side.book.socialing.domain.club.entity.Club
import com.side.book.socialing.domain.common.BaseEntity
import jakarta.persistence.*
import org.hibernate.annotations.SQLRestriction

@Entity
@Table(name = "club_review")
@SQLRestriction("deleted = false")
class ClubReview(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id", nullable = false)
    var club: Club,

    @Column(name = "user_id", nullable = false)
    var userId: Long, // TODO: User 엔티티 구현 후 @ManyToOne으로 변경 고려

    @Column(name = "rating", nullable = false)
    var rating: Int, // TINYINT는 Int 또는 Short로 매핑

    @Column(name = "content", columnDefinition = "TEXT")
    var content: String? = null,

    @Column(name = "deleted", nullable = false, columnDefinition = "boolean default false")
    var deleted: Boolean = false
) : BaseEntity() {

    fun delete() {
        deleted = true
    }
}
