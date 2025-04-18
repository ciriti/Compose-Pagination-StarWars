package com.example.messageapp.data.datasource.dto

data class MessageDto(
    val id: String,
    val chatId: String,
    val senderId: String,
    val content: String,
    val timestamp: Long,
    val status: MessageStatus
)

data class MessageEntity(
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

fun MessageEntity.toDto(): MessageDto {
    return MessageDto(
        id = id,
        chatId = chatId,
        senderId = senderId,
        content = content,
        timestamp = timestamp,
        status = status
    )
}
