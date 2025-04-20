package com.example.messageapp.data.repository

import com.example.messageapp.data.local.LocalMessageDataSource
import com.example.messageapp.data.local.MessageEntity
import com.example.messageapp.data.remote.RemoteMessageDataSource
import com.example.messageapp.data.remote.dto.Chat
import com.example.messageapp.data.remote.dto.MessageDto
import com.example.messageapp.data.remote.dto.MessageStatus
import com.example.messageapp.data.toDto
import com.example.messageapp.data.toEntity
import com.example.messageapp.domain.model.Message
import com.example.messageapp.domain.model.toEntity
import com.example.messageapp.domain.repository.MessageRepository
import com.example.messageapp.domain.sync.MessageSyncManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.channelFlow


class MessageRepositoryImpl(
    private val remoteMessageDataSource: RemoteMessageDataSource,
    private val localMessageDataSource: LocalMessageDataSource,
    private val connectivityRepository: ConnectivityRepository,
    private val syncManager: MessageSyncManager,
) : MessageRepository {
    override suspend fun sendMessage(message: Message): Result<Unit> = kotlin.runCatching {
        val messageEntity = message.toEntity(status = MessageStatus.SENDING)
        localMessageDataSource.insertMessage(messageEntity)
        syncManager.triggerImmediateSync()
    }

    override fun observeMessages(chatId: String): Flow<List<MessageEntity>> = channelFlow {
        localMessageDataSource
            .observeMessages(chatId)
            .collect(::send)

        if (connectivityRepository.isConnected.value) {
            remoteMessageDataSource
                .observeMessages(chatId)
                .catch { e -> /* Handle error */ }
                .collect { dto ->
                    dto.map(MessageDto::toEntity)
                        .let { localMessageDataSource.saveMessages(it) }
                }
        }
    }

    override suspend fun getChats(): Result<List<Chat>> = localMessageDataSource.getChats()

    override suspend fun getMessages(chatId: String): Flow<List<MessageEntity>> =
        localMessageDataSource
            .getMessages(chatId)

    override suspend fun syncPendingMessages() {
        if (!connectivityRepository.isConnected.value) return
        localMessageDataSource
            .getUnsentMessages()
            .onSuccess { unsent ->
                for (msg in unsent) {
                    remoteMessageDataSource.sendMessage(msg.toDto())
                        .onSuccess {
                            localMessageDataSource.updateMessageStatus(msg, MessageStatus.SENT)
                        }
                        .onFailure {
                            localMessageDataSource.updateMessageStatus(msg, MessageStatus.FAILED)
                        }
                }
            }

    }

    override suspend fun performFullSync() {
        try {
            // 1. Sync pending outgoing messages
            localMessageDataSource.getMessagesNeedingSync().onSuccess { messages ->
                messages.forEach { message ->
                    remoteMessageDataSource.sendMessage(message.toDto()).onSuccess {
                        localMessageDataSource.markMessageAsSynced(message.id)
                    }
                }
            }

            // 2. Sync incoming messages
            val lastSync = localMessageDataSource.getLastSyncTimestamp() ?: 0L
            remoteMessageDataSource.getMessagesSince(lastSync).onSuccess { newMessages ->
                localMessageDataSource.saveMessages(newMessages.map(MessageDto::toEntity))
                localMessageDataSource.updateLastSyncTimestamp(System.currentTimeMillis())
            }
        } catch (e: Exception) {
            // Handle error
        }
    }
}
