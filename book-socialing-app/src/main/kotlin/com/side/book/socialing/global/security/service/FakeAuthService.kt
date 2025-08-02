package com.side.book.socialing.global.security.service

import com.side.book.socialing.domain.user.command.CreateUserCommand
import com.side.book.socialing.domain.user.entity.User
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Service

@Service
@ConditionalOnProperty(name = ["spring.auth.active"], havingValue = "false")
class FakeAuthService : AuthService {

    override fun getAuthentication(token: String?): Authentication {
        val fakeUser = User.create(
            CreateUserCommand(
                provider = "test",
                providerId = "test_id",
                email = "test@example.com",
                nickName = "Test User",
                role = "User"
            )
        )

        return UsernamePasswordAuthenticationToken(
            fakeUser,
            null,
            setOf(SimpleGrantedAuthority(fakeUser.role))
        )
    }
}
