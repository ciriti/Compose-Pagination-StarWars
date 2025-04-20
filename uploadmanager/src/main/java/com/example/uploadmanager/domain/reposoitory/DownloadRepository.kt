package com.example.uploadmanager.domain.reposoitory

import com.example.uploadmanager.data.datasource.remote.model.DownloadProgress
import com.example.uploadmanager.data.model.DownloadStatus
import com.example.uploadmanager.data.model.DownloadTask
import kotlinx.coroutines.flow.Flow

interface DownloadRepository {
    fun getDownloadTasks(): Flow<List<DownloadTask>>
    suspend fun enqueueDownload(task: DownloadTask)
    suspend fun updateTaskStatus(taskId: String, status: DownloadStatus)
    suspend fun getPendingDownloadTasks(): List<DownloadTask>
    suspend fun observeProgressDownload(): Flow<List<DownloadProgress>>
}
