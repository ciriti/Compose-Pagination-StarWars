package com.example.trainmaintenancetracker.data.datasource.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "tasks",
    indices = [Index(value = ["taskId"], unique = true)]
)
data class TaskEntity(
    @PrimaryKey
    val taskId: String,
    val trainId: String,
    val taskType: String,
    val priorityLevel: String,
    val location: String,
    val dueDate: String,
    val description: String,
    val lastUpdated: Long = System.currentTimeMillis(),
    val isSynced: Boolean = false
)
