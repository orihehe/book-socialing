
package com.side.book.socialing.domain.user.service

import com.side.book.socialing.domain.user.repository.UserRepository
import com.side.book.socialing.global.Constants
import org.springframework.stereotype.Service
import kotlin.random.Random

@Service
class NicknameGenerationService(
    private val userRepository: UserRepository
) {

    fun generate(): String {
        var nickname: String
        do {
            val adjective = Constants.adjectives.random()
            val noun = Constants.nouns.random()
            nickname = "$adjective $noun ${Random.nextInt(1000, 9999)}"
        } while (userRepository.existsByNickname(nickname))
        return nickname
    }
}
