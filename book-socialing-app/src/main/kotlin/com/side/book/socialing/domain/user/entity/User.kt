package com.side.book.socialing.domain.user.entity

import com.side.book.socialing.domain.common.BaseEntity
import com.side.book.socialing.domain.user.command.CreateUserCommand
import com.side.book.socialing.domain.user.command.UpdateUserCommand
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import org.hibernate.annotations.SQLRestriction

@Entity
@Table(name = "users", uniqueConstraints = [UniqueConstraint(columnNames = ["provider", "provider_id"])])
@SQLRestriction("status = 'ACTIVE'")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false)
    val provider: String,

    @Column(name = "provider_id", nullable = false)
    var providerId: String,

    @Column(nullable = true)
    var email: String?,

    @Column(nullable = false)
    var nickname: String,

    @Column(name = "description", length = 1000)
    var description: String? = null,

    @Column(nullable = false)
    val role: String = "ROLE_USER",

    @Column(name = "profile_image_url", length = 500)
    var profileImageUrl: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: UserStatus = UserStatus.ACTIVE
) : BaseEntity() {

    companion object {
        fun create(cmd: CreateUserCommand): User {
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

    fun withdraw() {
        this.status = UserStatus.WITHDRAWN
        this.nickname = "탈퇴한 사용자"
        this.email = null
        this.profileImageUrl = null // or a default image
        this.providerId = "${this.providerId}_${System.currentTimeMillis()}"
    }

    fun update(cmd: UpdateUserCommand) {
        this.nickname = cmd.nickname
        this.description = cmd.description
    }

    fun updateFile(imageUrl: String) {
        this.profileImageUrl = imageUrl
    }
}
