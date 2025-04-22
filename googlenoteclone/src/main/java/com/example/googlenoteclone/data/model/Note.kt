package com.example.googlenoteclone.data.model

import java.util.UUID

data class Note(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val content: String,
    val type: NoteType,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val tags: List<Tag> = emptyList(),
    val isPinned: Boolean = false,
    val color: NoteColor = NoteColor.DEFAULT,
    val isArchived: Boolean = false,
    val isDeleted: Boolean = false,
    val syncStatus: SyncStatus = SyncStatus.PENDING,
    val attachments: List<Attachment> = emptyList()
)

enum class NoteType {
    TEXT,
    CHECKLIST,
    IMAGE,
    AUDIO,
    MIXED
}

enum class SyncStatus {
    PENDING,
    SYNCED,
    FAILED
}

enum class NoteColor(val hex: String) {
    DEFAULT("#FFFFFF"),
    RED("#FFCDD2"),
    BLUE("#BBDEFB"),
    GREEN("#C8E6C9"),
    YELLOW("#FFF9C4"),
    PURPLE("#E1BEE7")
}

data class Tag(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val color: TagColor = TagColor.DEFAULT,
    val createdAt: Long = System.currentTimeMillis()
)

enum class TagColor(val hex: String) {
    DEFAULT("#E0E0E0"),
    RED("#EF9A9A"),
    BLUE("#90CAF9"),
    GREEN("#A5D6A7"),
    YELLOW("#FFF59D"),
    PURPLE("#CE93D8")
}

sealed class Attachment {
    abstract val id: String
    abstract val noteId: String
    abstract val createdAt: Long
    abstract val uri: String

    data class ImageAttachment(
        override val id: String,
        override val noteId: String,
        override val uri: String,
        override val createdAt: Long = System.currentTimeMillis(),
        val caption: String? = null
    ) : Attachment()

    data class AudioAttachment(
        override val id: String,
        override val noteId: String,
        override val uri: String,
        override val createdAt: Long = System.currentTimeMillis(),
        val duration: Long = 0
    ) : Attachment()

    data class FileAttachment(
        override val id: String,
        override val noteId: String,
        override val uri: String,
        override val createdAt: Long = System.currentTimeMillis(),
        val mimeType: String
    ) : Attachment()
}
