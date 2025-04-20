package com.example.uploadmanager.data.model

import com.example.uploadmanager.data.datasource.local.entity.DownloadTaskEntity
import com.example.uploadmanager.data.datasource.remote.model.DownloadProgress


fun DownloadTaskEntity.toDownloadTask(): DownloadTask {
    return DownloadTask(
        id = id,
        fileName = fileName,
        url = url,
        destinationPath = destinationPath,
        status = status,
        totalBytes = totalBytes,
        downloadedBytes = downloadedBytes,
        createdAt = createdAt,
        updatedAt = updatedAt,
        retries = retries
    )
}

fun DownloadTask.toDownloadTaskEntity(): DownloadTaskEntity {
    return DownloadTaskEntity(
        id = id,
        fileName = fileName,
        url = url,
        destinationPath = destinationPath,
        status = status,
        totalBytes = totalBytes,
        downloadedBytes = downloadedBytes,
        createdAt = createdAt,
        updatedAt = updatedAt,
        retries = retries
    )
}

fun DownloadTaskEntity.toDownloadProgress(): DownloadProgress {
    return DownloadProgress(
        taskId = id,
        downloadedBytes = downloadedBytes,
        totalBytes = totalBytes,
        status = status,
        error = null,
        timestamp = updatedAt // Using updatedAt as timestamp
    )
}

fun DownloadProgress.toDownloadTaskEntity(): DownloadTaskEntity {
    return DownloadTaskEntity(
        id = taskId,
        url = "", // Not available in progress, needs separate handling
        fileName = "", // Not available in progress, needs separate handling
        destinationPath = "", // Not available in progress, needs separate handling
        status = status,
        downloadedBytes = downloadedBytes,
        totalBytes = totalBytes,
        updatedAt = timestamp
    )
}
