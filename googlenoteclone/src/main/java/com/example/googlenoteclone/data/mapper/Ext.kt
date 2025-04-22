package com.example.googlenoteclone.data.mapper

import com.example.googlenoteclone.data.model.Attachment
import com.example.googlenoteclone.data.model.Tag

import com.example.googlenoteclone.data.datasource.local.db.entity.NoteEntity
import com.example.googlenoteclone.data.datasource.local.db.entity.NoteType
import com.example.googlenoteclone.data.datasource.local.db.entity.SyncStatus
import com.example.googlenoteclone.data.model.Note
import com.example.googlenoteclone.data.model.NoteColor

// Domain to Entity
fun Note.toEntity(): NoteEntity = NoteEntity(
    id = id,
    title = title,
    content = content,
    type =  NoteType.TEXT,
    createdAt = createdAt,
    updatedAt = updatedAt,
    isSynced = syncStatus == com.example.googlenoteclone.data.model.SyncStatus.SYNCED,
    isDeleted = isDeleted,
    syncStatus = SyncStatus.PENDING
)

// Entity to Domain (requires tags and attachments as parameters)
fun NoteEntity.toDomain(
    tags: List<Tag> = emptyList(),
    attachments: List<Attachment> = emptyList()
): Note = Note(
    id = id,
    title = title,
    content = content,
    type = com.example.googlenoteclone.data.model.NoteType.TEXT,
//    when (type) {
//        NoteType.TEXT -> NoteType.TEXT
//        NoteType.CHECKLIST -> NoteType.CHECKLIST
//        NoteType.IMAGE -> NoteType.IMAGE
//        NoteType.AUDIO -> NoteType.AUDIO
//    },
    createdAt = createdAt,
    updatedAt = updatedAt,
    tags = tags,
    isPinned = false, // Not stored in entity
    color = NoteColor.DEFAULT, // Not stored in entity
    isArchived = false, // Not stored in entity
    isDeleted = isDeleted,
    syncStatus = com.example.googlenoteclone.data.model.SyncStatus.PENDING,
//    when (syncStatus) {
//        SyncStatus.PENDING -> SyncStatus.PENDING
//        SyncStatus.SYNCED -> SyncStatus.SYNCED
//        SyncStatus.FAILED -> SyncStatus.FAILED
//    },
    attachments = attachments
)

// List extensions for bulk operations
fun List<Note>.toEntities(): List<NoteEntity> = map { it.toEntity() }

fun List<NoteEntity>.toDomain(
    tagsMap: Map<String, List<Tag>> = emptyMap(),
    attachmentsMap: Map<String, List<Attachment>> = emptyMap()
): List<Note> = map { entity ->
    entity.toDomain(
        tags = tagsMap[entity.id] ?: emptyList(),
        attachments = attachmentsMap[entity.id] ?: emptyList()
    )
}
