package com.example.uploadmanager.data.model

import java.util.UUID

data class DownloadTask(
    val id: String = UUID.randomUUID().toString(),
    val fileName: String,
    val url: String,
    val destinationPath: String,
    val status: DownloadStatus,
    val totalBytes: Long,
    val downloadedBytes: Long,
    val createdAt: Long,
    val updatedAt: Long,
    val retries: Int
){
    val progress: Float
        get() = downloadedBytes.toFloat() / totalBytes.toFloat()
}
