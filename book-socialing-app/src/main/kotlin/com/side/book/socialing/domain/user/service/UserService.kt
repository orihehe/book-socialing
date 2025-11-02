package com.side.book.socialing.domain.user.service

import com.side.book.socialing.domain.user.dto.UserDto
import com.side.book.socialing.domain.user.repository.UserRepository
import com.side.book.socialing.global.utils.log
import com.side.book.socialing.presentation.user.dto.UserResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
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

    /**
     * 사용자 정보를 반환합니다.
     *
     * @return 사용자 정보가 담긴 `UserResponse` DTO 리스트.
     *         만약 노트가 없으면 null를 반환합니다
     */
    @Transactional(readOnly = true) // 읽기 전용 트랜잭션
    fun getUserProfileResponse(userId: Long): UserResponse? {
        val user = userRepository.findActiveUserByUserId(userId)
        if (user == null) {
            log.info("No user found. userId: $userId")
            return null
        }

        // 이미지 URL 목록 정제
        val imageUrls: List<String> = if (user.files.isNotEmpty()) {
            user.files.map { it.filePath } // 파일 엔티티의 filePath를 String 리스트로 변환
        } else {
            listOf("/images/default_book_image.jpg") // 기본 이미지 1개를 추가하는 예시
        }

        return UserResponse(
            id = user.id,
            email = user.email,
            nickname = user.nickname,
            description = user.description,
            role = user.role,
            imageUrls = imageUrls,
        )
    }
}
