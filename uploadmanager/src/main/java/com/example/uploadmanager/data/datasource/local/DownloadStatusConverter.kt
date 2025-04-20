package com.example.uploadmanager.data.datasource.local

import androidx.room.TypeConverter
import com.example.uploadmanager.data.model.DownloadStatus

class DownloadStatusConverter {
    @TypeConverter
    fun fromStatus(value: DownloadStatus): String = value.name

    @TypeConverter
    fun toStatus(value: String): DownloadStatus = DownloadStatus.valueOf(value)
}
