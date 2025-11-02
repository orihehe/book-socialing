package com.side.book.socialing.domain.user.entity

import com.side.book.socialing.domain.common.BaseEntity
import com.side.book.socialing.domain.user.command.CreateUserCommand
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import org.hibernate.annotations.BatchSize
import org.hibernate.annotations.SQLRestriction

@Entity
@Table(name = "users", uniqueConstraints = [UniqueConstraint(columnNames = ["provider", "provider_id"])])
@SQLRestriction("deleted = false")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false)
    val provider: String,

    @Column(name = "provider_id", nullable = false)
    val providerId: String,

    @Column(nullable = false)
    val email: String,

    @Column(nullable = false)
    var nickname: String,

    @Column(name = "profile_image_url")
    var profileImageUrl: String? = null,

    @Column(name = "description", length = 1000)
    var description: String? = null,

    @Column(nullable = false)
    val role: String = "ROLE_USER",

    @BatchSize(size = 100)
    @OneToMany(mappedBy = "user")
    var files: List<UserFile> = listOf(),

    @Column(name = "deleted", nullable = false)
    var deleted: Boolean = false

) : BaseEntity() {

    companion object {
        fun create(cmd: CreateUserCommand, id: Long? = null): User {
            return User(
                provider = cmd.provider,
                providerId = cmd.providerId,
                email = cmd.email,
                nickname = cmd.nickName,
                description = cmd.description,
                role = cmd.role
            )
        }
    }

    fun update(nickname: String, profileImageUrl: String?, description: String?) {
        this.nickname = nickname
        this.profileImageUrl = profileImageUrl
        this.description = description
    }
}
