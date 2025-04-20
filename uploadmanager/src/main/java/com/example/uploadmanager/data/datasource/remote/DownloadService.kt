package com.example.uploadmanager.data.datasource.remote

import com.example.uploadmanager.data.datasource.local.dao.DownloadTaskDao
import com.example.uploadmanager.data.datasource.remote.model.DownloadProgress
import com.example.uploadmanager.data.model.DownloadStatus
import com.example.uploadmanager.data.model.DownloadTask
import com.example.uploadmanager.data.model.toDownloadTask
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flattenMerge
import kotlinx.coroutines.flow.onStart
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.util.concurrent.ConcurrentHashMap

interface DownloadService {
    fun downloadTask(task: DownloadTask): Flow<DownloadProgress>
    suspend fun cancelDownload(taskId: String)


    suspend fun executePendingDownloads()
    suspend fun cancelAll()
}

internal class DownloadServiceImpl(
    private val client: OkHttpClient,
    private val dao: DownloadTaskDao,
    private val maxConcurrentDownloads: Int,
) : DownloadService {

    private val activeDownloads = ConcurrentHashMap<String, okhttp3.Call>()

    override suspend fun executePendingDownloads() {

        dao.getPendingDownloadTasks()
            .map { task ->
                downloadTask(task.toDownloadTask())
                    .onStart { dao.updateDownloadTask(task.copy(status = DownloadStatus.IN_PROGRESS)) }
                    .catch { dao.updateDownloadTask(task.copy(status = DownloadStatus.FAILED)) }
            }
            .asFlow()
            .flattenMerge(concurrency = maxConcurrentDownloads)
            .collect { progress ->
                when (progress.status) {
                    DownloadStatus.IN_PROGRESS -> {} // update progress and status
                    DownloadStatus.COMPLETE -> {} // mark as complete
                    DownloadStatus.FAILED -> {} // mark as failed
                    else -> {}
                }
            }

    }

    override suspend fun cancelAll() {
        activeDownloads.keys().toList().forEach { cancelDownload(it) }
    }

    override suspend fun cancelDownload(taskId: String) {
        activeDownloads[taskId]?.cancel()
        activeDownloads.remove(taskId)
    }

    override fun downloadTask(task: DownloadTask): Flow<DownloadProgress> = callbackFlow {

        // create the file
        val file = File(task.destinationPath, task.fileName)
        // create the request
        try {
            val request = Request.Builder().url(task.url).build()
            client
                .newCall(request)
                .also { activeDownloads[task.id] = it }
                .execute().use { response ->
                    if (!response.isSuccessful) throw Exception("Failed to download")

                    val body = response.body!!
                    val contentLength = body.contentLength()
                    var bytesDownloaded = 0L

                    file.outputStream().use { output ->
                        body.byteStream().use { input ->
                            val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
                            var bytesRead: Int

                            while (input.read(buffer).also { bytesRead = it } != -1) {
                                output.write(buffer, 0, bytesRead)
                                bytesDownloaded += bytesRead

                                trySend(
                                    DownloadProgress(
                                        task.id,
                                        bytesDownloaded,
                                        contentLength,
                                        DownloadStatus.IN_PROGRESS,
                                        null
                                    )
                                )
                            }
                        }
                    }

                    trySend(
                        DownloadProgress(
                            task.id,
                            contentLength,
                            contentLength,
                            DownloadStatus.COMPLETE,
                            null
                        )
                    )
                }
        } catch (e: Exception) {
            trySend(
                DownloadProgress(
                    task.id,
                    0,
                    0,
                    DownloadStatus.FAILED,
                    e
                )
            )
            throw e
        } finally {
            activeDownloads.remove(task.id)
            close()
        }
    }


}
