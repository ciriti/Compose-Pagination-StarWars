package com.example.messageapp.domain.repository

import com.example.messageapp.data.local.MessageEntity
import com.example.messageapp.data.remote.dto.Chat
import com.example.messageapp.domain.model.Message
import kotlinx.coroutines.flow.Flow

interface MessageRepository {
    suspend fun sendMessage(message: Message): Result<Unit>
    fun observeMessages(chatId: String): Flow<List<MessageEntity>>
    suspend fun getChats(): Result<List<Chat>>
    suspend fun getMessages(chatId: String): Flow<List<MessageEntity>>
    suspend fun syncPendingMessages()
    suspend fun performFullSync()
}
