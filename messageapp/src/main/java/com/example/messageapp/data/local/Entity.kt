package com.example.messageapp.data.local

import com.example.messageapp.data.remote.dto.MessageStatus

data class MessageEntity(
    val id: String,
    val chatId: String,
    val senderId: String,
    val content: String,
    val timestamp: Long,
    val status: MessageStatus
)

data class ChatEntity(
    val id: String,
    val name: String,
    val lastMessage: String,
    val lastMessageTime: Long,
    val unreadCount: Int,
    val isPinned: Boolean,
    val isMuted: Boolean
)
