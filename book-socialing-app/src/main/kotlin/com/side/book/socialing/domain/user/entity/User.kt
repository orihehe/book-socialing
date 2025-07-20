package com.side.book.socialing.domain.user.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@Entity
@EntityListeners(AuditingEntityListener::class)
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
    var nickname: String,

    @Column(nullable = false)
    val role: String = "ROLE_USER",

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
)
