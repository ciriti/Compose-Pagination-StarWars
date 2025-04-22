package com.example.googlenoteclone.data.datasource.remote

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query


interface NoteApiService {
    @POST("notes")
    suspend fun createNote(
        @Header("Authorization") auth: String,
        @Body note: NoteDto
    ): NoteDto

    @PUT("notes/{id}")
    suspend fun updateNote(
        @Header("Authorization") auth: String,
        @Path("id") id: String,
        @Body note: NoteDto
    ): NoteDto

    @DELETE("notes/{id}")
    suspend fun deleteNote(
        @Header("Authorization") auth: String,
        @Path("id") id: String
    )

    @POST("notes/sync")
    suspend fun syncNotes(
        @Header("Authorization") auth: String,
        @Body notes: List<NoteDto>
    ): List<NoteDto>

    @GET("notes/recent")
    suspend fun getRecentNotes(
        @Header("Authorization") auth: String,
        @Query("since") since: Long
    ): List<NoteDto>

    @GET("notes")
    suspend fun getAllNotes(
        @Header("Authorization") auth: String
    ): List<NoteDto>

    @POST("attachments")
    suspend fun uploadAttachment(
        @Header("Authorization") auth: String,
        @Body attachment: AttachmentDto
    ): AttachmentDto

    @DELETE("attachments/{id}")
    suspend fun deleteAttachment(
        @Header("Authorization") auth: String,
        @Path("id") id: String
    )

    @POST("tags/sync")
    suspend fun syncTags(
        @Header("Authorization") auth: String,
        @Body tags: List<TagDto>
    ): List<TagDto>
}
