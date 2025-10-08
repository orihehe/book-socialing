package com.side.book.socialing.presentation

import com.side.book.socialing.global.test.TestService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/test")
class TestController(
    private val testService: TestService
) {

    @GetMapping
    fun test() {
        testService.createTestData()
    }
}
