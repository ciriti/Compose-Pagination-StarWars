package com.example.messageapp.data.local

import com.example.messageapp.data.remote.dto.Chat
import com.example.messageapp.data.remote.dto.MessageStatus
import kotlinx.coroutines.flow.Flow

interface LocalMessageDataSource {
    suspend fun insertMessage(message: MessageEntity)
    suspend fun getChats(): Result<List<Chat>>
    suspend fun getMessages(chatId: String): Flow<List<MessageEntity>>
    suspend fun updateMessageStatus(messageId: MessageEntity, status: MessageStatus): MessageEntity
    suspend fun getPendingMessages(): Result<List<MessageEntity>>
    suspend fun getUnsentMessages(): Result<List<MessageEntity>>
    fun observeMessages(chatId: String): Flow<List<MessageEntity>>

    suspend fun saveMessages(messages: List<MessageEntity>): Result<Unit>
    suspend fun getMessagesNeedingSync(): Result<List<MessageEntity>>
    suspend fun markMessageAsSynced(messageId: String)
    suspend fun getLastSyncTimestamp(): Long?
    suspend fun updateLastSyncTimestamp(timestamp: Long)
}
