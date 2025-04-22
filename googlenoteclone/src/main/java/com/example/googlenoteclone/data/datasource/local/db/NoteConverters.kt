package com.example.googlenoteclone.data.datasource.local.db

import androidx.room.TypeConverter
import com.example.googlenoteclone.data.datasource.local.db.entity.SyncStatus

class NoteConverters {
    @TypeConverter
    fun fromNoteType(type: NoteType): String = type.name

    @TypeConverter
    fun toNoteType(value: String): NoteType = NoteType.valueOf(value)

    @TypeConverter
    fun fromSyncStatus(status: SyncStatus): String = status.name

    @TypeConverter
    fun toSyncStatus(value: String): SyncStatus = SyncStatus.valueOf(value)
}

enum class NoteType {
    TEXT,
    CHECKLIST,
    IMAGE,
    AUDIO,
    DRAWING
}
