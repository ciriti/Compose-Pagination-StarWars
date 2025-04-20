package com.example.messageapp.data.sync

import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.messageapp.domain.sync.MessageSyncManager
import java.util.concurrent.TimeUnit

internal class MessageSyncManagerImpl(
//    private val messageRepository: MessageRepository,
//    private val connectivityRepository: ConnectivityRepository,
//    private val scope: CoroutineScope,
    private val workManager: WorkManager,
//    private val context: Context
) : MessageSyncManager {

    companion object {
        private const val SYNC_TAG = "message_sync"
        private const val SYNC_WORK_NAME = "message_sync_work"
    }

    override fun monitorNetworkAndSync() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val syncWork =
            PeriodicWorkRequestBuilder<MessageSyncWorker>(1, TimeUnit.HOURS) // Default interval
                .setConstraints(constraints)
                .addTag(SYNC_TAG)
                .build()

        workManager.enqueueUniquePeriodicWork(
            SYNC_WORK_NAME,
            ExistingPeriodicWorkPolicy.UPDATE,
            syncWork
        )
    }

    override fun schedulePeriodicSync(intervalHours: Long) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val syncWork = PeriodicWorkRequestBuilder<MessageSyncWorker>(
            intervalHours, TimeUnit.HOURS
        ).setConstraints(constraints)
            .build()

        workManager.enqueueUniquePeriodicWork(
            SYNC_WORK_NAME,
            ExistingPeriodicWorkPolicy.UPDATE,
            syncWork
        )
    }

    override fun triggerImmediateSync() {
        val syncWork = OneTimeWorkRequestBuilder<MessageSyncWorker>()
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .addTag(SYNC_TAG)
            .build()

        workManager.enqueue(syncWork)
    }

    override fun cancelPeriodicSync() {
        workManager.cancelUniqueWork(SYNC_WORK_NAME)
    }
}
