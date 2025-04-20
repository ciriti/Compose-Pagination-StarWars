package com.example.uploadmanager.data.repository

import com.example.uploadmanager.data.datasource.local.dao.DownloadTaskDao
import com.example.uploadmanager.data.datasource.local.entity.DownloadTaskEntity
import com.example.uploadmanager.data.datasource.remote.model.DownloadProgress
import com.example.uploadmanager.data.model.DownloadStatus
import com.example.uploadmanager.data.model.DownloadTask
import com.example.uploadmanager.data.model.toDownloadProgress
import com.example.uploadmanager.data.model.toDownloadTask
import com.example.uploadmanager.data.model.toDownloadTaskEntity
import com.example.uploadmanager.data.sync.DownloadSyncManager
import com.example.uploadmanager.domain.reposoitory.DownloadRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DownloadRepositoryImpl(
    private val dao: DownloadTaskDao,
    private val downloadSyncManager: DownloadSyncManager,
) : DownloadRepository {

    companion object {
        private const val MAX_RETRIES = 3
    }

    override fun getDownloadTasks(): Flow<List<DownloadTask>> {
        return dao.getAllDownloadTasks()
            .map { it.map(DownloadTaskEntity::toDownloadTask) }
    }

    override suspend fun enqueueDownload(task: DownloadTask) {
        dao.insertDownloadTask(task.toDownloadTaskEntity())
        downloadSyncManager.triggerImmediateSync()

    }

    override suspend fun updateTaskStatus(taskId: String, status: DownloadStatus) {
        val task = dao.getDownloadTaskById(taskId)
        dao.updateDownloadTask(task.copy(status = status))
    }

    override suspend fun getPendingDownloadTasks(): List<DownloadTask> {
        return dao
            .getPendingDownloadTasks(status = DownloadStatus.PENDING)
            .map(DownloadTaskEntity::toDownloadTask)
    }

    override suspend fun observeProgressDownload(): Flow<List<DownloadProgress>> = dao
        .getAllDownloadTasks()
        .map { list -> list.map { it.toDownloadProgress() } }
}
