package com.example.trainmaintenancetracker.domain.data.datasource.local

import com.example.trainmaintenancetracker.data.datasource.local.TaskEntity
import kotlinx.coroutines.flow.Flow

interface LocalTaskDataSource {
    fun observeAllTasks(): Flow<List<TaskEntity>>
    suspend fun getTaskById(taskId: String): Result<TaskEntity>
    suspend fun getTasks(): Result<List<TaskEntity>>
    suspend fun insertTasks(tasks: List<TaskEntity>)
}
