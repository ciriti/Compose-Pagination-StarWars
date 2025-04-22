package com.example.googlenoteclone.data.datasource.remote

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class RemoteNoteDataSourceImpl(
    private val noteApiService: NoteApiService,
    private val okHttpClient: OkHttpClient,
    private val authToken: String,
    private val baseUrl: String
) : RemoteNoteDataSource {

    private val authHeader = "Bearer $authToken"

    override suspend fun createNote(note: NoteDto): NoteDto {
        return noteApiService.createNote(authHeader, note)
    }

    override suspend fun updateNote(note: NoteDto): NoteDto {
        return noteApiService.updateNote(authHeader, note.id, note)
    }

    override suspend fun deleteNote(noteId: String) {
        noteApiService.deleteNote(authHeader, noteId)
    }

    override suspend fun syncNotes(notes: List<NoteDto>): List<NoteDto> {
        return noteApiService.syncNotes(authHeader, notes)
    }

    override suspend fun getRecentNotes(since: Long): List<NoteDto> {
        return noteApiService.getRecentNotes(authHeader, since)
    }

    override fun observeNotes(): Flow<List<NoteDto>> = flow {
        emit(noteApiService.getAllNotes(authHeader))
    }

    override fun observeNoteChanges(): Flow<NoteDto> = callbackFlow {
        val url = baseUrl.toHttpUrl().newBuilder()
            .addPathSegment("notes")
            .addPathSegment("updates")
            .build()

        val request = Request.Builder()
            .url(url)
            .header("Authorization", "Bearer $authToken")
            .build()

        val webSocket = okHttpClient.newWebSocket(request, object : WebSocketListener() {
            override fun onMessage(webSocket: WebSocket, text: String) {
                try {
                    val noteUpdate: NoteDto? = null// = Json.decodeFromString<NoteDto>(text)
                    trySend(noteUpdate!!)
                } catch (e: Exception) {
                    close(e)
                }
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                close(t)
            }
        })

        awaitClose {
            webSocket.close(1000, "Closing WebSocket")
        }
    }

    override suspend fun uploadAttachment(attachment: AttachmentDto): AttachmentDto {
        return noteApiService.uploadAttachment(authHeader, attachment)
    }

    override suspend fun deleteAttachment(attachmentId: String) {
        noteApiService.deleteAttachment(authHeader, attachmentId)
    }

    override suspend fun syncTags(tags: List<TagDto>): List<TagDto> {
        return noteApiService.syncTags(authHeader, tags)
    }
}
