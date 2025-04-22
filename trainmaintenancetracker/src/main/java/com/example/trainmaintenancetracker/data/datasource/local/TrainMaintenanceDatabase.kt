package com.example.trainmaintenancetracker.data.datasource.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [TaskEntity::class],
    version = 1,
    exportSchema = false
)
abstract class TrainMaintenanceDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao

    companion object {
        @Volatile
        private var INSTANCE: TrainMaintenanceDatabase? = null

        fun getInstance(context: Context): TrainMaintenanceDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    TrainMaintenanceDatabase::class.java,
                    "task_database"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
