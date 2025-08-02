package com.side.book.socialing.global.error

import com.side.book.socialing.global.security.exception.JwtAuthenticationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

data class ErrorResponse(val message: String?)

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(JwtAuthenticationException::class)
    fun handleJwtAuthenticationException(e: JwtAuthenticationException): ResponseEntity<ErrorResponse> {
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(ErrorResponse(e.message))
    }
}
