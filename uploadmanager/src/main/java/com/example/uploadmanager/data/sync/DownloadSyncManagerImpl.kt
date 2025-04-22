package com.example.uploadmanager.data.sync

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit


interface DownloadSyncManager {
    fun monitorNetworkAndSync()
    fun schedulePeriodicSync(intervalHours: Long)
    fun triggerImmediateSync()
    fun cancelPeriodicSync()
}

class DownloadSyncManagerImpl(
    private val workManager: WorkManager,
    private val context: Context,
) : DownloadSyncManager {

    companion object {
        private const val UNIQUE_SYNC_WORK_NAME = "download_sync_work"
        private const val MIN_PERIODIC_INTERVAL_HOURS = 4L
    }

    override fun triggerImmediateSync() {
        // sync request
        val request = OneTimeWorkRequestBuilder<DownloadSyncWorker>()
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .setConstraints(getNetworkConstraints())
            .build()

        workManager.enqueueUniqueWork(
            request = request,
            uniqueWorkName = UNIQUE_SYNC_WORK_NAME,
            existingWorkPolicy = ExistingWorkPolicy.APPEND_OR_REPLACE
        )
    }

    override fun monitorNetworkAndSync() {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        cm.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: android.net.Network) {
                if (isNetworkSuitableForDownloads(cm.getNetworkCapabilities(network))) {
                    triggerImmediateSync()
                }
            }
        })
    }

    override fun schedulePeriodicSync(intervalHours: Long) {
        val periodicRequest = PeriodicWorkRequestBuilder<DownloadSyncWorker>(
            intervalHours.coerceAtLeast(MIN_PERIODIC_INTERVAL_HOURS),
            TimeUnit.HOURS
        )
            .setConstraints(getNetworkConstraints())
            .build()
        workManager.enqueueUniquePeriodicWork(
            UNIQUE_SYNC_WORK_NAME,
            ExistingPeriodicWorkPolicy.UPDATE,
            periodicRequest
        )
    }

    override fun cancelPeriodicSync() {
        workManager.cancelUniqueWork(UNIQUE_SYNC_WORK_NAME)
    }

    private fun getNetworkConstraints(): Constraints {
        return Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .setRequiresBatteryNotLow(true)
            .build()
    }

    private fun isNetworkSuitableForDownloads(capabilities: NetworkCapabilities?): Boolean {
        return capabilities?.let {
            it.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                    it.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) &&
                    !it.hasCapability(NetworkCapabilities.NET_CAPABILITY_TEMPORARILY_NOT_METERED)
        } ?: false
    }
}
