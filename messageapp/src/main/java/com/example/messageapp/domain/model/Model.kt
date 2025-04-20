package com.example.messageapp.domain.model

import com.example.messageapp.data.datasource.dto.MessageEntity
import com.example.messageapp.data.remote.dto.MessageStatus

data class Message(
    val id: String,
    val chatId: String,
    val senderId: String,
    val content: String,
    val timestamp: Long,
    val status: MessageStatus
)


fun Message.toEntity(status: MessageStatus = this.status): MessageEntity {
    return MessageEntity(
        id = id,
        chatId = chatId,
        senderId = senderId,
        content = content,
        timestamp = timestamp,
        status = status
    )
}
