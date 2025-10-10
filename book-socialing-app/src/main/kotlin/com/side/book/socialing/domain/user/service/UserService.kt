package com.side.book.socialing.domain.user.service

import com.side.book.socialing.domain.user.dto.UserDto
import com.side.book.socialing.domain.user.repository.UserRepository
import com.side.book.socialing.global.utils.log
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

@Service
class UserService(
    private val userRepository: UserRepository
) {
    fun getUserMap(userIds: Set<Long>): Map<Long, UserDto> {
        val users = userRepository.findAllById(userIds)
        return users.map {
            UserDto.from(it)
        }.associateBy { it.id }
    }

    fun getUser(userId: Long): UserDto? {
        val user = userRepository.findById(userId).getOrNull()
        if (user == null) {
            log.info("No user found. userId: $userId")
            return null
        }
        return UserDto.from(user)
    }
}
