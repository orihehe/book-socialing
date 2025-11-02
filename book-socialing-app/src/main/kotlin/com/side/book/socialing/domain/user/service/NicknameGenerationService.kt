
package com.side.book.socialing.domain.user.service

import com.side.book.socialing.domain.user.repository.UserRepository
import com.side.book.socialing.global.Constants
import com.side.book.socialing.global.utils.log
import org.springframework.stereotype.Service
import java.util.UUID
import kotlin.random.Random

@Service
class NicknameGenerationService(
    private val userRepository: UserRepository
) {

    fun generate(): String {
        repeat(10) {
            val adjective = Constants.adjectives.random()
            val noun = Constants.nouns.random()
            val nickname = "$adjective $noun ${Random.nextInt(1000, 9999)}"
            if (!userRepository.existsByNickname(nickname)) {
                return nickname
            }
        }
        log.warn("Failed to generate a unique nickname after 10 attempts.")
        return "user-${UUID.randomUUID()}"
    }
}
