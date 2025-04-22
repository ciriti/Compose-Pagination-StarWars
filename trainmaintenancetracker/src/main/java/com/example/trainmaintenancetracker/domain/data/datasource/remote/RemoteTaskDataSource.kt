package com.example.trainmaintenancetracker.domain.data.datasource.remote

import com.example.trainmaintenancetracker.data.datasource.remote.model.TaskDto

interface RemoteTaskDataSource {
    suspend fun getAllTasks(): Result<List<TaskDto>>
}
