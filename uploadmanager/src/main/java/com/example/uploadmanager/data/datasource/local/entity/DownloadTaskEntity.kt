package com.example.uploadmanager.data.datasource.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.uploadmanager.data.model.DownloadStatus
import java.util.UUID

@Entity(tableName = "download_tasks")
data class DownloadTaskEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val url: String,
    val fileName: String,
    val destinationPath: String,
    val status: DownloadStatus = DownloadStatus.PENDING,
    val downloadedBytes: Long = 0,
    val totalBytes: Long = 0,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val retries: Int = 0
)
