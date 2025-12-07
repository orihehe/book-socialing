package com.side.book.socialing

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.retry.annotation.EnableRetry

@EnableRetry
@SpringBootApplication
class SocialingApplication

fun main(args: Array<String>) {
    runApplication<SocialingApplication>(*args)
}
