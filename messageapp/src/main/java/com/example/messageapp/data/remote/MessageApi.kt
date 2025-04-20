package com.example.messageapp.data.remote

import com.example.messageapp.data.remote.dto.Chat
import com.example.messageapp.data.remote.dto.MessageDto
import retrofit2.Response
import retrofit2.http.*

interface MessageApi {
    @POST("messages")
    suspend fun sendMessage(
        @Header("Authorization") authorization: String,
        @Body message: MessageDto
    ): Response<Unit>

    @GET("messages/{chatId}")
    suspend fun getMessages(
        @Header("Authorization") authorization: String,
        @Path("chatId") chatId: String,
        @Query("limit") limit: Int = 100,
        @Query("offset") offset: Int = 0
    ): Response<List<MessageDto>>

    @GET("messages/sync")
    suspend fun getMessagesSince(
        @Header("Authorization") authorization: String,
        @Query("since") since: Long,
        @Query("chatId") chatId: String? = null
    ): Response<List<MessageDto>>

    @GET("chats")
    suspend fun getChats(
        @Header("Authorization") authorization: String,
        @Query("updatedSince") updatedSince: Long? = null
    ): Response<List<Chat>>

    @GET("messages/{messageId}/status")
    suspend fun checkMessageStatus(
        @Header("Authorization") authorization: String,
        @Path("messageId") messageId: String
    ): Response<MessageStatusResponse>

    @PUT("messages/{messageId}/read")
    suspend fun markAsRead(
        @Header("Authorization") authorization: String,
        @Path("messageId") messageId: String
    ): Response<Unit>

    data class MessageStatusResponse(
        val messageId: String,
        val status: String, // "SENT", "DELIVERED", "READ"
        val timestamp: Long
    )
}
