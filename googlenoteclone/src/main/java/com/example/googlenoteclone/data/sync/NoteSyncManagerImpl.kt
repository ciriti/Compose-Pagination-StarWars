package com.example.googlenoteclone.data.sync

import android.content.Context
import androidx.work.WorkManager
import com.example.googlenoteclone.data.datasource.LocalNoteDataSource
import com.example.googlenoteclone.data.datasource.local.db.entity.NoteEntity
import com.example.googlenoteclone.data.datasource.remote.NoteDto
import com.example.googlenoteclone.data.datasource.remote.RemoteNoteDataSource
import com.example.googlenoteclone.data.model.SyncStatus
import kotlinx.coroutines.flow.Flow

class NoteSyncManagerImpl(
    private val workManager: WorkManager,
    private val context: Context,
    private val localDataSource: LocalNoteDataSource,
    private val remoteDataSource: RemoteNoteDataSource
): NoteSyncManager {

    companion object {
        private const val SYNC_TAG = "message_sync"
        private const val SYNC_WORK_NAME = "message_sync_work"
        private const val MIN_SYNC_INTERVAL_HOURS = 1L
    }

    override suspend fun syncChanges(localNotes: List<NoteEntity>, remoteNotes: List<NoteDto>) {
        TODO("Not yet implemented")
    }

    override fun monitorNetworkAndSync() {
        TODO("Not yet implemented")
    }

    override fun schedulePeriodicSync(intervalHours: Long) {
        TODO("Not yet implemented")
    }

    override fun triggerImmediateSync() {
        TODO("Not yet implemented")
    }

    override fun cancelPeriodicSync() {
        TODO("Not yet implemented")
    }

    override suspend fun resolveConflicts(
        localNotes: List<NoteEntity>,
        remoteNotes: List<NoteDto>
    ) {
        TODO("Not yet implemented")
    }

    override val lastSyncTime: Flow<Long>
        get() = TODO("Not yet implemented")
    override val syncStatus: Flow<SyncStatus>
        get() = TODO("Not yet implemented")

    override suspend fun retryFailedSyncs() {
        TODO("Not yet implemented")
    }
}
