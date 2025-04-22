package com.example.googlenoteclone.data.datasource.remote

import com.example.googlenoteclone.data.model.Note
import com.example.googlenoteclone.data.model.NoteColor
import com.example.googlenoteclone.data.model.NoteType

/**
 * Data Transfer Object for Note when communicating with backend services
 */
data class NoteDto(
    val id: String,
    val title: String,
    val content: String,
    val type: String, // "TEXT", "CHECKLIST", etc.
    val createdAt: Long,
    val updatedAt: Long,
    val ownerId: String? = null,
    val tags: List<TagDto> = emptyList(),
    val isPinned: Boolean = false,
    val color: String? = null, // Hex color code
    val isArchived: Boolean = false,
    val isDeleted: Boolean = false,
    val version: Int = 1, // For conflict resolution
    val attachments: List<AttachmentDto> = emptyList(),
    val checklistItems: List<ChecklistItemDto>? = null
) {
    companion object {
        const val TYPE_TEXT = "TEXT"
        const val TYPE_CHECKLIST = "CHECKLIST"
        const val TYPE_IMAGE = "IMAGE"
        const val TYPE_AUDIO = "AUDIO"
    }
}

/**
 * Simplified version for creating new notes
 */
data class CreateNoteDto(
    val title: String,
    val content: String,
    val type: String,
    val tags: List<String> = emptyList() // Tag IDs
)

/**
 * For updating existing notes
 */
data class UpdateNoteDto(
    val id: String,
    val title: String? = null,
    val content: String? = null,
    val tags: List<String>? = null,
    val isPinned: Boolean? = null,
    val color: String? = null,
    val isArchived: Boolean? = null,
    val version: Int
)

/**
 * Tag DTO for network operations
 */
data class TagDto(
    val id: String,
    val name: String,
    val color: String? = null,
    val createdAt: Long? = null
)

/**
 * Checklist item DTO
 */
data class ChecklistItemDto(
    val id: String,
    val text: String,
    val isChecked: Boolean,
    val position: Int
)

/**
 * Attachment DTO with metadata
 */
data class AttachmentDto(
    val id: String,
    val noteId: String,
    val type: String, // "IMAGE", "AUDIO", etc.
    val uri: String,
    val uploadStatus: String = "PENDING", // "UPLOADED", "FAILED"
    val metadata: Map<String, String> = emptyMap(),
    val createdAt: Long = System.currentTimeMillis()
)

/**
 * Sync response containing changes
 */
data class SyncResponseDto(
    val notes: List<NoteDto>,
    val conflicts: List<ConflictDto> = emptyList(),
    val serverTimestamp: Long
)

/**
 * For conflict resolution
 */
data class ConflictDto(
    val noteId: String,
    val localVersion: Int,
    val serverVersion: Int,
    val serverNote: NoteDto?
)

// Converting from domain model to DTO
fun Note.toDto(): NoteDto = NoteDto(
    id = id,
    title = title,
    content = content,
    type = type.name,
    createdAt = createdAt,
    updatedAt = updatedAt,
    tags = emptyList(),// tags.map { it.toDto() },
    isPinned = isPinned,
    color = color.hex,
    attachments = emptyList(),// attachments.map { it.toDto() }
)

// Converting from DTO to domain model
fun NoteDto.toDomain(): Note = Note(
    id = id,
    title = title,
    content = content,
    type = NoteType.valueOf(type),
    createdAt = createdAt,
    updatedAt = updatedAt,
    tags = emptyList(),// tags.map { it.toDomain() },
    isPinned = isPinned,
    color = NoteColor.RED
)
