package com.example.messageapp.data.datasource.local

import com.example.messageapp.data.datasource.dto.Chat
import com.example.messageapp.data.datasource.dto.MessageEntity
import com.example.messageapp.data.datasource.dto.MessageStatus
import kotlinx.coroutines.flow.Flow

interface LocalMessageDataSource {
    suspend fun insertMessage(message: MessageEntity)
    suspend fun getChats(): Result<List<Chat>>
    suspend fun updateMessageStatus(messageId: MessageEntity, status: MessageStatus): MessageEntity
    suspend fun getPendingMessages(): Result<List<MessageEntity>>
    suspend fun getUnsentMessages(): Result<List<MessageEntity>>
    fun observeMessages(chatId: String): Flow<List<MessageEntity>>
}
