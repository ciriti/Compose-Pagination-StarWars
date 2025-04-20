package com.example.uploadmanager.data.datasource.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.uploadmanager.data.datasource.local.entity.DownloadTaskEntity

@Database(
    entities = [DownloadTaskEntity::class],
    version = 1
)
@TypeConverters(DownloadStatusConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun downloadTaskDao(): DownloadTaskDao
}
