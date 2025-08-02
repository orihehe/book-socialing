package com.side.book.socialing.global.security.service

import org.springframework.security.core.Authentication

interface AuthService {
    fun getAuthentication(token: String?): Authentication
}
