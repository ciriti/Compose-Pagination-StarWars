package com.example.messageapp.data

import com.example.messageapp.data.remote.dto.MessageDto
import com.example.messageapp.data.local.MessageEntity


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

fun MessageDto.toEntity(): MessageEntity {
    return MessageEntity(
        id = id,
        chatId = chatId,
        senderId = senderId,
        content = content,
        timestamp = timestamp,
        status = status
    )
}
