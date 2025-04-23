package com.example.trainmaintenancetracker.data

import com.example.trainmaintenancetracker.data.datasource.local.TaskEntity
import com.example.trainmaintenancetracker.data.datasource.remote.model.TaskDto

val testEntity1 = TaskEntity("1", "train1", "Brake", "High", "Location1", "2023-01-01", "Desc1")
val testEntity2 = TaskEntity("2", "train2", "Engine", "Medium", "Location2", "2023-01-02", "Desc2")
val testEntities = listOf(
    testEntity1,
    testEntity2,
)


val testTaskDto1 = TaskDto(
    taskId = "1",
    trainId = "train1",
    taskType = "Brake",
    priorityLevel = "High",
    location = "Location1",
    dueDate = "2023-01-01",
    description = "Desc1"
)

val testTaskDtos = listOf(
    testTaskDto1,
    TaskDto(
        taskId = "2",
        trainId = "train2",
        taskType = "Engine",
        priorityLevel = "Medium",
        location = "Location2",
        dueDate = "2023-01-02",
        description = "Desc2"
    )
)
