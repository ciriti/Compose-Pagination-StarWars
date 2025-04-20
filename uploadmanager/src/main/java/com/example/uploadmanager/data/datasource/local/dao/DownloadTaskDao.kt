package com.example.uploadmanager.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.uploadmanager.data.datasource.local.entity.DownloadTaskEntity
import com.example.uploadmanager.data.model.DownloadStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface DownloadTaskDao {

    @Query("SELECT * FROM download_tasks ORDER BY createdAt DESC")
    fun getAllDownloadTasks(): Flow<List<DownloadTaskEntity>>

    @Query("SELECT * FROM download_tasks WHERE status = :status")
    fun getPendingDownloadTasksFlow(status: DownloadStatus = DownloadStatus.PENDING): Flow<List<DownloadTaskEntity>>

    @Query("SELECT * FROM download_tasks WHERE id = :taskId")
    suspend fun getDownloadTaskById(taskId: String): DownloadTaskEntity

    @Query("SELECT * FROM download_tasks WHERE status = :status")
    suspend fun getPendingDownloadTasks(status: DownloadStatus = DownloadStatus.PENDING): List<DownloadTaskEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDownloadTask(task: DownloadTaskEntity)

    @Update
    suspend fun updateDownloadTask(task: DownloadTaskEntity)

    @Delete
    suspend fun deleteDownloadTask(task: DownloadTaskEntity)

    @Query("DELETE FROM download_tasks WHERE status = :status")
    suspend fun deleteTasksByStatus(status: DownloadStatus)
}
