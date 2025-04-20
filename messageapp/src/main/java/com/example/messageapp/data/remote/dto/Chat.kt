package com.example.messageapp.data.remote.dto

data class Chat(
    val id: String,
    val name: String,
    val lastMessage: String,
    val lastMessageTime: Long,
    val unreadCount: Int,
    val isPinned: Boolean,
    val isMuted: Boolean
)
