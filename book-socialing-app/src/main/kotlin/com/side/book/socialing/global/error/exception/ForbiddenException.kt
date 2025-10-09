package com.side.book.socialing.global.error.exception

/**
 * 사용자가 인증은 되었지만, 특정 리소스 또는 기능에 접근할 권한이 없을 때 발생하는 예외.
 * HTTP 403 Forbidden 상태 코드와 매핑됩니다.
 */
class ForbiddenException(message: String) : RuntimeException(message)
