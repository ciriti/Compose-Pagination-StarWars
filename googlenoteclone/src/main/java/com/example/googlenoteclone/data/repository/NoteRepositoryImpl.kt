package com.example.googlenoteclone.data.repository

import com.example.googlenoteclone.data.datasource.LocalNoteDataSource
import com.example.googlenoteclone.data.datasource.remote.RemoteNoteDataSource
import com.example.googlenoteclone.data.mapper.toDomain
import com.example.googlenoteclone.data.mapper.toEntity
import com.example.googlenoteclone.data.model.Note
import com.example.googlenoteclone.data.sync.NoteSyncManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow


class NoteRepositoryImpl(
    private val localDataSource: LocalNoteDataSource,
    private val remoteDataSource: RemoteNoteDataSource,
    private val syncManager: NoteSyncManager,
    private val connectivityRepository: ConnectivityRepository
) : NoteRepository {

    override suspend fun createNote(note: Note) {
        localDataSource.insertNote(note.toEntity())
        syncManager.triggerImmediateSync()
    }

    override fun observeNotes(): Flow<List<Note>> {
        return channelFlow {
            localDataSource.observeNotes().collect { localNotes ->
                trySend(localNotes.map { it.toDomain() })

                if (connectivityRepository.isConnected.value) {
                    remoteDataSource.observeNotes().collect { remoteNotes ->
                        syncManager.syncChanges(localNotes, remoteNotes)
                    }
                }
            }
        }
    }
}
