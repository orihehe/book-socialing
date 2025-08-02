package com.side.book.socialing.global.security.exception

import org.springframework.security.core.AuthenticationException


class JwtAuthenticationException(
    message: String
) : AuthenticationException(message)
