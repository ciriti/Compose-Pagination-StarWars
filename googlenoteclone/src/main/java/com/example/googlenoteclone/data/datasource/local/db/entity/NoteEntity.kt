package com.example.googlenoteclone.data.datasource.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val title: String,
    val content: String,
    val type: NoteType, // TEXT, CHECKLIST, IMAGE, AUDIO
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val isSynced: Boolean = false,
    val isDeleted: Boolean = false,
    val syncStatus: SyncStatus = SyncStatus.PENDING
)

enum class SyncStatus { PENDING, SYNCED, FAILED }
enum class NoteType { TEXT, CHECKLIST, IMAGE, AUDIO }
