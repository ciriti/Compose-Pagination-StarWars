package com.example.trainmaintenancetracker.data.datasource.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [TaskEntity::class],
    version = 1,
)
abstract class TrainMaintenanceDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}
