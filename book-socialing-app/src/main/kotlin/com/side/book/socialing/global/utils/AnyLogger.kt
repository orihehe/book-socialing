package com.side.book.socialing.global.utils

import com.nimbusds.jose.shaded.gson.Gson
import com.nimbusds.jose.shaded.gson.GsonBuilder
import org.slf4j.Logger
import org.slf4j.LoggerFactory

val <R: Any> R.logger
    get() = lazy { LoggerFactory.getLogger(this::class.java) }

val <R: Any> R.log: Logger
    get() = logger.value

class AnyLogger {
    companion object {
        private val gson: Gson = GsonBuilder().serializeNulls().create()

        fun log(obj: Any): String {
            return try {
                gson.toJson(obj)
            } catch (e: Exception) {
                log.warn("[AnyLogger] ${e.message}", e)
                e.message!!
            }
        }
    }
}
