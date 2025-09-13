package com.side.book.socialing.global.auth

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component

@Component
@ConditionalOnProperty(name = ["spring.auth.active"], havingValue = "false")
class MockUserPrincipalResolver : UserPrincipalResolver {
    override fun getUserId(): Long {
        return 1L
    }
}
