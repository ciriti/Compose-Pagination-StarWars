package com.example.trainmaintenancetracker.data.datasource.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object TrainMaintenanceNetworkClient {
    private const val BASE_URL = "https://gist.githubusercontent.com/"

    val apiService: TaskApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TaskApiService::class.java)
    }
}
