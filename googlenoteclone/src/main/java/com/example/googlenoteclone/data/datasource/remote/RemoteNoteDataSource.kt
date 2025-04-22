package com.example.googlenoteclone.data.datasource.remote

import kotlinx.coroutines.flow.Flow

interface RemoteNoteDataSource {
    // Basic operations
    suspend fun createNote(note: NoteDto): NoteDto
    suspend fun updateNote(note: NoteDto): NoteDto
    suspend fun deleteNote(noteId: String)

    // Bulk operations
    suspend fun syncNotes(notes: List<NoteDto>): List<NoteDto>
    suspend fun getRecentNotes(since: Long): List<NoteDto>

    // Real-time updates
    fun observeNotes(): Flow<List<NoteDto>>
    fun observeNoteChanges(): Flow<NoteDto>

    // Attachments
    suspend fun uploadAttachment(attachment: AttachmentDto): AttachmentDto
    suspend fun deleteAttachment(attachmentId: String)

    // Tags
    suspend fun syncTags(tags: List<TagDto>): List<TagDto>
}
