package com.example.messageapp.domain.repository

import com.example.messageapp.data.datasource.dto.Chat
import com.example.messageapp.data.datasource.dto.MessageEntity
import kotlinx.coroutines.flow.Flow

interface MessageRepository {
    suspend fun sendMessage(messageDto: MessageEntity): Result<Unit>
    fun observeMessages(chatId: String): Flow<List<MessageEntity>>
    suspend fun getChats(): Result<List<Chat>>
    suspend fun getMessages(chatId: String): Result<List<MessageEntity>>
    suspend fun syncPendingMessages()
}
