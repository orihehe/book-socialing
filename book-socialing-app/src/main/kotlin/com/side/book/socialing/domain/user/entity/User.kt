package com.side.book.socialing.domain.user.entity

import com.side.book.socialing.domain.common.BaseEntity
import com.side.book.socialing.domain.user.command.CreateUserCommand
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import org.hibernate.annotations.SQLRestriction

@Entity
@Table(name = "users", uniqueConstraints = [UniqueConstraint(columnNames = ["provider", "provider_id"])])
@SQLRestriction("deleted = false")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    val provider: String,

    @Column(name = "provider_id", nullable = false)
    val providerId: String,

    @Column(nullable = false)
    val email: String,

    @Column(nullable = false)
    val nickname: String,

    @Column(nullable = false)
    val role: String = "ROLE_USER",

    @Column(name = "deleted", nullable = false)
    var deleted: Boolean = false

) : BaseEntity() {

    companion object {
        fun create(cmd: CreateUserCommand, id: Long? = null): User {
            return User(
                id = id,
                provider = cmd.provider,
                providerId = cmd.providerId,
                email = cmd.email,
                nickname = cmd.nickName,
                role = cmd.role
            )
        }
    }

    fun delete(userId: Long) {
        deleted = true
    }
}
