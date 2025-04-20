package com.example.uploadmanager.data.datasource.remote.model

import com.example.uploadmanager.data.model.DownloadStatus

data class DownloadProgress(
    val taskId: String,
    val downloadedBytes: Long,
    val totalBytes: Long,
    val status: DownloadStatus,
    val error: Throwable?,
    val timestamp: Long = System.currentTimeMillis()
){
    val progress: Float
        get() = downloadedBytes.toFloat() / totalBytes.toFloat()
}
