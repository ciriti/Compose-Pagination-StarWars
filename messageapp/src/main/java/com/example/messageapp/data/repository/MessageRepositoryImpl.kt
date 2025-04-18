package com.example.messageapp.data.repository

import com.example.messageapp.data.datasource.dto.Chat
import com.example.messageapp.data.datasource.dto.MessageEntity
import com.example.messageapp.data.datasource.dto.MessageStatus
import com.example.messageapp.data.datasource.dto.toDto
import com.example.messageapp.data.datasource.local.LocalMessageDataSource
import com.example.messageapp.data.datasource.remote.RemoteMessageDataSource
import com.example.messageapp.domain.repository.MessageRepository
import kotlinx.coroutines.flow.Flow


class MessageRepositoryImpl(
    private val remoteMessageDataSource: RemoteMessageDataSource,
    private val localMessageDataSource: LocalMessageDataSource,

    ) : MessageRepository {
    override suspend fun sendMessage(messageDto: MessageEntity): Result<Unit> {
        TODO("Not yet implemented")
    }

    override fun observeMessages(chatId: String): Flow<List<MessageEntity>> {
        TODO("Not yet implemented")
    }

    override suspend fun getChats(): Result<List<Chat>> {
        TODO("Not yet implemented")
    }

    override suspend fun getMessages(chatId: String): Result<List<MessageEntity>> {
        TODO("Not yet implemented")
    }

    override suspend fun syncPendingMessages() {
        localMessageDataSource.getUnsentMessages().onSuccess { unsent ->
            for (msg in unsent) {
                remoteMessageDataSource.sendMessage(msg.toDto()).onSuccess {
                    localMessageDataSource.updateMessageStatus(msg, MessageStatus.SENT)
                }.onFailure {
                    localMessageDataSource.updateMessageStatus(msg, MessageStatus.FAILED)
                }
            }
        }

    }
}
