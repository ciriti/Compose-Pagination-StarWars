package com.example.googlenoteclone.data.repository

import com.example.googlenoteclone.data.model.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    suspend fun createNote(note: Note)
    fun observeNotes(): Flow<List<Note>>
}
