package com.side.book.socialing.domain.user.repository

import com.side.book.socialing.domain.user.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface UserRepository : JpaRepository<User, Long> {
    fun findByEmail(email: String): User?

    fun existsByNickname(nickname: String): Boolean

    @Query("SELECT u FROM User u WHERE u.id = :userId AND u.deleted = false")
    fun findActiveUserByUserId(userId: Long): User?
}
