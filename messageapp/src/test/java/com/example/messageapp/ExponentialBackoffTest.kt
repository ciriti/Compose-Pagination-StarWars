package com.example.messageapp

import kotlinx.coroutines.delay

class ExponentialBackoffTest {

    suspend fun exBackoff(
        maxRetries: Int,
        block: suspend () -> Unit
    ) {
        var delayTime = 100L
        repeat(maxRetries) {
            try {
                return block()
            } catch (e: Exception) {
                println("failed attempt")
                delayTime *= 2
            }
            delay(delayTime)

        }

    }
}
