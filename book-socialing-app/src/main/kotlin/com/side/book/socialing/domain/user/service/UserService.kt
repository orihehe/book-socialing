package com.side.book.socialing.domain.user.service

import com.side.book.socialing.domain.user.command.UpdateUserCommand
import com.side.book.socialing.domain.user.dto.UserDto
import com.side.book.socialing.domain.user.event.UserWithdrawnEvent
import com.side.book.socialing.domain.user.repository.UserRepository
import com.side.book.socialing.global.file.FileUploader
import com.side.book.socialing.global.file.StoredFile
import com.side.book.socialing.global.utils.log
import com.side.book.socialing.infrastructure.oauth.OAuthClientManager
import com.side.book.socialing.presentation.user.dto.UserResponse
import jakarta.persistence.EntityNotFoundException
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.nio.file.Paths

@Service
class UserService(
    private val userRepository: UserRepository,
    private val oAuthClientManager: OAuthClientManager,
    private val fileUploader: FileUploader,
    private val eventPublisher: ApplicationEventPublisher,
    @Value("\${file.user-dir}") private val filePath: String
) {
    fun getUserMap(userIds: Set<Long>): Map<Long, UserDto> {
        val users = userRepository.findAllById(userIds)
        return users.map {
            UserDto.from(it)
        }.associateBy { it.id }
    }

    fun getUser(userId: Long): UserDto? {
        val user = userRepository.findByIdOrNull(userId)
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
        val user = userRepository.findByIdOrNull(userId)
        if (user == null) {
            log.info("No user found. userId: $userId")
            return null
        }

        return UserResponse(
            id = user.id,
            email = user.email,
            nickname = user.nickname,
            description = user.description,
            role = user.role,
            imageUrl = user.profileImageUrl
        )
    }

    /**
     * 사용자 정보를 수정합니다.
     *
     * @return 사용자 정보가 담긴 `UserResponse` DTO 리스트.
     *         만약 노트가 없으면 null를 반환합니다
     */
    @Transactional // 읽기 전용 트랜잭션
    fun updateUser(cmd: UpdateUserCommand) {
        val user = userRepository.findByIdOrNull(cmd.userId)
            ?: throw EntityNotFoundException("User with ID ${cmd.userId} not found")

        user.update(cmd)

        val userFilePath = Paths.get(filePath, user.id!!.toString()).toString()

        var uploadedFile: StoredFile? = null
        try {
            cmd.imageFile?.let { file ->
                // 파일 업로드
                val storedFile = fileUploader.upload(file, userFilePath)
                uploadedFile = storedFile

                user.updateFile(storedFile.filePath)
            }
        } catch (e: Exception) {
            // 업로드된 파일이 있다면 삭제
            uploadedFile?.let { fileUploader.delete(it.filePath) }
            throw e
        }
    }

    @Transactional
    suspend fun withdrawUser(userId: Long) {
        val user = userRepository.findByIdOrNull(userId)
            ?: throw EntityNotFoundException("User with ID $userId not found")

        try {
            oAuthClientManager.unlink(user.provider, user.providerId)
        } catch (e: Exception) {
            log.error("Failed to process unlink for user: $userId", e)
            throw IllegalStateException("Failed to withdraw user due to unlink failure.", e)
        }

        user.withdraw()
        eventPublisher.publishEvent(UserWithdrawnEvent(userId))
    }
}
