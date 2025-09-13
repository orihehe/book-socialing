package com.side.book.socialing.global.auth

import com.side.book.socialing.global.security.principal.UserPrincipal
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
@ConditionalOnProperty(name = ["spring.auth.active"], havingValue = "true", matchIfMissing = true)
class SecurityContextUserPrincipalResolver : UserPrincipalResolver {
    override fun getUserId(): Long {
        val authentication = SecurityContextHolder.getContext().authentication
        val principal = authentication.principal

        if (principal is UserPrincipal) {
            return principal.id
        }

        // TODO: 추후 인증 안된 사용자에 대한 예외 처리 필요
        throw IllegalStateException("User not authenticated")
    }
}
