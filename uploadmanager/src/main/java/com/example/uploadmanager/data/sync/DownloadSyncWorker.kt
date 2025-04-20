package com.example.uploadmanager.data.sync

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.example.uploadmanager.data.datasource.remote.DownloadService
import com.example.uploadmanager.domain.reposoitory.DownloadRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class DownloadSyncWorker(
    private val context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params), KoinComponent {

    private val syncService: DownloadService by inject()

    private val MAX_RETRIES = 3

    override suspend fun getForegroundInfo(): ForegroundInfo = context.syncForegroundInfo()

    override suspend fun doWork(): Result {
        if (runAttemptCount > 0) {
            setForegroundAsync(getForegroundInfo())
        }
        return performSync()
    }

    private suspend fun performSync(): Result {
        return try {
            syncService.executePendingDownloads()
            Result.success()
        } catch (e: Exception) {
            if (runAttemptCount < MAX_RETRIES) Result.retry() else Result.failure()
        }
    }
}
