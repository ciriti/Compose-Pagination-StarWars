package com.example.trainmaintenancetracker.ui.screen.tasks

import com.example.trainmaintenancetracker.domain.model.Task

val testTask1 = Task(
    taskId = "1",
    trainId = "TR-123",
    taskType = "Inspection",
    priorityLevel = "High",
    location = "Depot A",
    dueDate = "2023-12-01",
    description = "Full inspection required"
)
val testData2 = Task(
    taskId = "2",
    trainId = "TR-456",
    taskType = "Maintenance",
    priorityLevel = "Medium",
    location = "Depot B",
    dueDate = "2023-12-02",
    description = "Routine maintenance"
)
