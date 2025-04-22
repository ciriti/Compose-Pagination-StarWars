package com.example.trainmaintenancetracker.data.datasource.local

import com.example.trainmaintenancetracker.domain.data.datasource.local.LocalTaskDataSource
import kotlinx.coroutines.flow.Flow

class RoomTaskDataSourceImpl(
    private val taskDao: TaskDao
) : LocalTaskDataSource {

    override fun observeAllTasks(): Flow<List<TaskEntity>> {
        return taskDao.observeAllTasks()
    }

    override suspend fun getTaskById(taskId: String): Result<TaskEntity> = runCatching {
        taskDao.getTaskById(taskId)
            ?: throw Exception("Task not found") // TODO: Replace with custom exception
    }

    override suspend fun insertTasks(tasks: List<TaskEntity>) {
        taskDao.insertTasks(tasks)
    }

    override suspend fun getTasks(): Result<List<TaskEntity>> = runCatching {
        taskDao.getAllTasks()
    }
}
