package com.example.messageapp.data.remote.dto

data class MessageDto(
    val id: String,
    val chatId: String,
    val senderId: String,
    val content: String,
    val timestamp: Long,
    val status: MessageStatus
)

enum class MessageStatus {
    SENDING, SENT, DELIVERED, READ, FAILED
}
