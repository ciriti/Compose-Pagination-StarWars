package com.example.trainmaintenancetracker.data.sync

import com.example.trainmaintenancetracker.domain.data.repository.ConnectivityRepository
import com.example.trainmaintenancetracker.domain.data.datasource.local.LocalTaskDataSource
import com.example.trainmaintenancetracker.domain.data.datasource.remote.RemoteTaskDataSource
import com.example.trainmaintenancetracker.domain.mapper.toEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

interface TaskSyncManager {
    fun schedulePeriodicSync(intervalHours: Long)
    fun triggerImmediateSync()
    fun cancelPeriodicSync()
}


class TaskSyncManagerImpl(
    // private val workManager: WorkManager, // In a production app, I'd use WorkManager here
    private val localDataSource: LocalTaskDataSource,
    private val remoteDataSource: RemoteTaskDataSource,
    private val connectivityRepository: ConnectivityRepository,
    private val coroutineScope: CoroutineScope, // with workmanager this param is not necessary
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO, // with workmanager this param is not necessary
) : TaskSyncManager {

    override fun schedulePeriodicSync(intervalHours: Long) {
        // sync the data periodically
    }

    override fun triggerImmediateSync() {
        if (!connectivityRepository.isConnected.value) return
        coroutineScope.launch(ioDispatcher) {
            remoteDataSource
                .getAllTasks()
                .onSuccess {
                    val entityTas = it.map { dto -> dto.toEntity() }
                    localDataSource.insertTasks(entityTas)
                }
        }

    }

    override fun cancelPeriodicSync() {
        // cancel the periodic sync
    }
}
