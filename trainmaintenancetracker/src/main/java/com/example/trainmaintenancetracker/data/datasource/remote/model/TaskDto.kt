package com.example.trainmaintenancetracker.data.datasource.remote.model

import com.google.gson.annotations.SerializedName


data class TaskResponse(
    @SerializedName("tasks")
    val tasks: List<TaskDto>
)

data class TaskDto(
    val taskId: String,
    val trainId: String,
    val taskType: String,
    val priorityLevel: String,
    val location: String,
    val dueDate: String,
    val description: String,
    val lastUpdated: Long = System.currentTimeMillis()
)
