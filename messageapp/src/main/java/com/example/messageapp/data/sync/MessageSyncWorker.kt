package com.example.messageapp.data.sync

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.messageapp.domain.repository.MessageRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MessageSyncWorker(
    context: Context,
    params: WorkerParameters,
) : CoroutineWorker(context, params), KoinComponent {

    private val messageRepository by inject<MessageRepository>()

    override suspend fun doWork(): Result {
        return try {
            messageRepository.syncPendingMessages()
            Result.success()
        } catch (e: Exception) {
            if (runAttemptCount < 3) {
                Result.retry()
            } else
                Result.failure()
        }
    }
}
