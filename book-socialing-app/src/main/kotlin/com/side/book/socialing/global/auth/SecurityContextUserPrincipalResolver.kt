package com.side.book.socialing.global.auth

import com.side.book.socialing.domain.user.entity.User
import com.side.book.socialing.global.error.exception.UserNotAuthenticatedException
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
@ConditionalOnProperty(name = ["spring.auth.active"], havingValue = "true", matchIfMissing = true)
class SecurityContextUserPrincipalResolver : UserPrincipalResolver {
    override fun getUserId(): Long {
        val authentication = SecurityContextHolder.getContext().authentication
        val principal = authentication.principal

        if (principal is User) {
            return principal.id!!
        }

        throw UserNotAuthenticatedException("User not authenticated")
    }
}
