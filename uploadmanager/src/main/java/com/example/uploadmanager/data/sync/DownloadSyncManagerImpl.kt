package com.example.uploadmanager.data.sync

import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkManager


interface DownloadSyncManager {
    fun monitorNetworkAndSync()
    fun schedulePeriodicSync(intervalHours: Long)
    fun triggerImmediateSync()
    fun cancelPeriodicSync()
}

class DownloadSyncManagerImpl(
    private val workManager: WorkManager,
) : DownloadSyncManager {

    override fun triggerImmediateSync() {
        // sync request
        val request = OneTimeWorkRequestBuilder<DownloadSyncWorker>()
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .build()

        workManager.enqueueUniqueWork(
            request = request,
            uniqueWorkName = "download_sync_work",
            existingWorkPolicy = ExistingWorkPolicy.APPEND_OR_REPLACE
        )
    }

    override fun monitorNetworkAndSync() {
        TODO("Not yet implemented")
    }

    override fun schedulePeriodicSync(intervalHours: Long) {
        TODO("Not yet implemented")
    }

    override fun cancelPeriodicSync() {
        TODO("Not yet implemented")
    }
}
