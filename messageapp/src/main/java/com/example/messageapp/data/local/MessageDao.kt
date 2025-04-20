package com.example.messageapp.data.local

import androidx.room.Dao
import com.example.messageapp.data.remote.dto.Chat

@Dao
interface MessageDao {
    suspend fun insertMessage(message: MessageEntity)
    suspend fun getChats(): List<Chat>
    fun observeMessages(chatId: String): List<MessageEntity>
}
