package com.example.trainmaintenancetracker.data.datasource.remote

import com.example.trainmaintenancetracker.data.datasource.remote.model.TaskResponse
import retrofit2.Response
import retrofit2.http.GET

interface TaskApiService {
    @GET("mootazltaief/6f6ae202071449386b57eb3876ce25ee/raw/dfa58b73b5fea4951276f89e09b4267d81c0895b/tasks.kt")
    suspend fun getAllTasks(): Response<TaskResponse>
}
