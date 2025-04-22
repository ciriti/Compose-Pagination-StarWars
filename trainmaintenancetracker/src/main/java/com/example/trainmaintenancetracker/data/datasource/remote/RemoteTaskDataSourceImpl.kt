package com.example.trainmaintenancetracker.data.datasource.remote

import com.example.trainmaintenancetracker.data.datasource.remote.model.TaskDto
import com.example.trainmaintenancetracker.domain.data.datasource.remote.RemoteTaskDataSource
import com.example.trainmaintenancetracker.domain.getException
import retrofit2.Response

class RemoteTaskDataSourceImpl(
    private val taskApiService: TaskApiService,
    private val exceptionMapper: (Response<*>) -> Exception = { r -> getException(r) },
) : RemoteTaskDataSource {

    override suspend fun getAllTasks(): Result<List<TaskDto>> = runCatching {
        val response = taskApiService.getAllTasks()
        if (response.isSuccessful) {
            val taskResponse = response.body() ?: throw Exception("Empty response")
            taskResponse.tasks
        } else {
            throw exceptionMapper(response)
        }

    }

}
