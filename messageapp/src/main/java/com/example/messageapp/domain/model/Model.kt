package com.example.messageapp.domain.model

import com.example.messageapp.data.datasource.dto.MessageStatus

data class Message(
    val id: String,
    val chatId: String,
    val senderId: String,
    val content: String,
    val timestamp: Long,
    val status: MessageStatus
)
