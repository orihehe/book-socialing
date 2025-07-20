package com.side.book.socialing.domain.user.repository

import com.side.book.socialing.domain.user.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<User, Long> {
    fun findByEmail(email: String): User?
}
