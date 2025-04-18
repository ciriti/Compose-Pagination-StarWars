package com.example.messageapp.data.datasource.local

import androidx.room.Dao
import com.example.messageapp.data.datasource.dto.Chat
import com.example.messageapp.data.datasource.dto.MessageEntity

@Dao
interface MessageDao {
    suspend fun insertMessage(message: MessageEntity)
    suspend fun getChats(): List<Chat>
    fun observeMessages(chatId: String): List<MessageEntity>
}
