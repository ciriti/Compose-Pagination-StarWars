package com.example.googlenoteclone.data.sync

import com.example.googlenoteclone.data.datasource.local.db.entity.NoteEntity
import com.example.googlenoteclone.data.datasource.remote.NoteDto
import com.example.googlenoteclone.data.model.SyncStatus
import kotlinx.coroutines.flow.Flow


interface NoteSyncManager {

    suspend fun syncChanges(localNotes: List<NoteEntity>, remoteNotes: List<NoteDto>)

    // Automatic sync
    fun monitorNetworkAndSync()
    fun schedulePeriodicSync(intervalHours: Long)

    // Manual sync control
    fun triggerImmediateSync()
    fun cancelPeriodicSync()

    // Conflict resolution
    suspend fun resolveConflicts(localNotes: List<NoteEntity>, remoteNotes: List<NoteDto>)

    // Status
    val lastSyncTime: Flow<Long>
    val syncStatus: Flow<SyncStatus>

    // Error handling
    suspend fun retryFailedSyncs()
}
