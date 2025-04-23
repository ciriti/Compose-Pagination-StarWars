package com.example.trainmaintenancetracker.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class Task(
    val taskId: String,
    val trainId: String,
    val taskType: String,
    val priorityLevel: String,
    val location: String,
    val dueDate: String,
    val description: String
)

val EmptyTask = Task(
    taskId = "",
    trainId = "",
    taskType = "",
    priorityLevel = "",
    location = "",
    dueDate = "",
    description = ""
)

val Task.isNotEmpty: Boolean
    get() = this != EmptyTask
