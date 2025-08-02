package com.side.book.socialing.domain.user.entity

import com.side.book.socialing.domain.common.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint

@Entity
@Table(name = "users", uniqueConstraints = [UniqueConstraint(columnNames = ["provider", "provider_id"])])
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val provider: String,

    @Column(name = "provider_id", nullable = false)
    val providerId: String,

    @Column(nullable = false)
    val email: String,

    @Column(nullable = false)
    val nickname: String,

    @Column(nullable = false)
    val role: String = "ROLE_USER"
) : BaseEntity()
