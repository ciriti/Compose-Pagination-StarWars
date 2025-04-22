package com.example.trainmaintenancetracker.data.repository

import com.example.trainmaintenancetracker.data.datasource.local.TaskEntity
import com.example.trainmaintenancetracker.data.sync.TaskSyncManager
import com.example.trainmaintenancetracker.domain.data.datasource.local.LocalTaskDataSource
import com.example.trainmaintenancetracker.domain.data.repository.TaskRepository
import com.example.trainmaintenancetracker.domain.mapper.toDomain
import com.example.trainmaintenancetracker.domain.model.Task
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class TaskRepositoryImpl(
    private val localDataSource: LocalTaskDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : TaskRepository {

    override fun observeAllTasks(): Flow<List<Task>> = localDataSource
        .observeAllTasks()
        .map { it.map(TaskEntity::toDomain) }
        .flowOn(ioDispatcher)

    override suspend fun getTasks(): Result<List<Task>> = withContext(ioDispatcher) {
        localDataSource
            .getTasks()
            .map { it.map(TaskEntity::toDomain) }
    }

    override suspend fun getTaskById(taskId: String): Result<Task> = withContext(ioDispatcher) {
        localDataSource
            .getTaskById(taskId)
            .map { it.toDomain() }
    }
}
