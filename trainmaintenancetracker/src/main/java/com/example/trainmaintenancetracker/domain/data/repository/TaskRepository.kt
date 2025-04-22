package com.example.trainmaintenancetracker.domain.data.repository

import com.example.trainmaintenancetracker.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    suspend fun getTasks(): Result<List<Task>>
    suspend fun getTaskById(taskId: String): Result<Task>
    fun observeAllTasks(): Flow<List<Task>>
}
