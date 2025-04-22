package com.example.trainmaintenancetracker.domain.mapper

import com.example.trainmaintenancetracker.data.datasource.local.TaskEntity
import com.example.trainmaintenancetracker.data.datasource.remote.model.TaskDto
import com.example.trainmaintenancetracker.domain.model.Task

fun TaskEntity.toDto(): TaskDto {
    return TaskDto(
        taskId = taskId,
        trainId = trainId,
        taskType = taskType,
        priorityLevel = priorityLevel,
        location = location,
        dueDate = dueDate,
        description = description,
        lastUpdated = lastUpdated,
    )
}

fun TaskDto.toEntity(): TaskEntity {
    return TaskEntity(
        taskId = taskId,
        trainId = trainId,
        taskType = taskType,
        priorityLevel = priorityLevel,
        location = location,
        dueDate = dueDate,
        description = description,
        isSynced = true,
        lastUpdated = lastUpdated,
    )
}

fun TaskEntity.toDomain(): Task {
    return Task(
        taskId = taskId,
        trainId = trainId,
        taskType = taskType,
        priorityLevel = priorityLevel,
        location = location,
        dueDate = dueDate,
        description = description
    )
}

fun TaskEntity.toDomainResult(): Result<Task> = runCatching { toDomain() }
