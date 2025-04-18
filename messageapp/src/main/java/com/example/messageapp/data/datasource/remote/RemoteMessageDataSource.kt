package com.example.messageapp.data.datasource.remote

import com.example.messageapp.data.datasource.dto.MessageDto
import com.example.messageapp.domain.security.SecureStorage
import kotlinx.coroutines.flow.Flow

interface RemoteMessageDataSource {
    suspend fun sendMessage(messageDto: MessageDto): Result<Unit>
    fun observeMessages(chatId: String): Flow<List<MessageDto>>
}

class RemoteMessageDataSourceImpl(
    val secureStorage: SecureStorage,

    ): RemoteMessageDataSource{
    override suspend fun sendMessage(messageDto: MessageDto): Result<Unit> {
        TODO("Not yet implemented")
    }

    override fun observeMessages(chatId: String): Flow<List<MessageDto>> {
        TODO("Not yet implemented")
    }
}
