package com.example.googlenoteclone.data.datasource

import com.example.googlenoteclone.data.datasource.local.db.entity.NoteEntity
import com.example.googlenoteclone.data.datasource.local.db.entity.TagEntity
import kotlinx.coroutines.flow.Flow

interface LocalNoteDataSource {
    // Basic CRUD operations
    suspend fun insertNote(note: NoteEntity)
    suspend fun updateNote(note: NoteEntity)
    suspend fun deleteNote(noteId: String)
    suspend fun getNoteById(noteId: String): NoteEntity?

    // Bulk operations
    suspend fun insertNotes(notes: List<NoteEntity>)
    suspend fun getUnsyncedNotes(): List<NoteEntity>
    suspend fun markNotesAsSynced(noteIds: List<String>)

    // Observers
    fun observeNotes(): Flow<List<NoteEntity>>
    fun observeNoteById(noteId: String): Flow<NoteEntity?>

    // Search and filtering
    suspend fun searchNotes(query: String): List<NoteEntity>
    suspend fun getNotesByTag(tagId: String): List<NoteEntity>
    suspend fun getPinnedNotes(): List<NoteEntity>

    // Tags
    suspend fun insertTag(tag: TagEntity)
    suspend fun getTagsForNote(noteId: String): List<TagEntity>
}
