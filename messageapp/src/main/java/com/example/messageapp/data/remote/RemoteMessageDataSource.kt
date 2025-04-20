package com.example.messageapp.data.remote

import com.example.messageapp.data.remote.dto.Chat
import com.example.messageapp.data.remote.dto.MessageDto
import com.example.messageapp.domain.security.SecureStorage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import retrofit2.HttpException

interface RemoteMessageDataSource {
    suspend fun sendMessage(messageDto: MessageDto): Result<Unit>
    suspend fun getMessages(chatId: String): Result<List<MessageDto>>
    suspend fun getMessagesSince(timestamp: Long): Result<List<MessageDto>>
    suspend fun getChats(): Result<List<Chat>>
    fun observeMessages(chatId: String): Flow<List<MessageDto>>
}

class RemoteMessageDataSourceImpl(
    private val secureStorage: SecureStorage,
    private val messageApi: MessageApi
) : RemoteMessageDataSource {
    override suspend fun sendMessage(messageDto: MessageDto): Result<Unit> = kotlin.runCatching {
        val authToken = secureStorage.getAuthToken()
            ?: return Result.failure(IllegalStateException("Not authenticated"))

        messageApi.sendMessage(
            authorization = "Bearer $authToken",
            message = messageDto
        ).let { response ->
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(HttpException(response))
            }
        }
    }

    override fun observeMessages(chatId: String): Flow<List<MessageDto>> {
        // Implement using WebSocket or Firebase if real-time updates needed
        TODO("Not yet implemented")
    }

    override suspend fun getMessages(chatId: String): Result<List<MessageDto>> =
        kotlin.runCatching {
            val authToken = secureStorage.getAuthToken()
                ?: return Result.failure(IllegalStateException("Not authenticated"))

            messageApi.getMessages(
                authorization = "Bearer $authToken",
                chatId = chatId
            ).let { response ->
                if (response.isSuccessful) {

                    response.body() ?: emptyList()
                } else {
                    throw HttpException(response)
                }
            }
        }

    override suspend fun getMessagesSince(timestamp: Long): Result<List<MessageDto>> =
        kotlin.runCatching {
            val authToken = secureStorage.getAuthToken()
                ?: return Result.failure(IllegalStateException("Not authenticated"))

            messageApi.getMessagesSince(
                authorization = "Bearer $authToken",
                since = timestamp
            ).let { response ->
                if (response.isSuccessful) {
                    response.body() ?: emptyList()
                } else {
                    throw HttpException(response)
                }
            }
        }

    override suspend fun getChats(): Result<List<Chat>> = kotlin.runCatching {
        val authToken = secureStorage.getAuthToken()
            ?: return Result.failure(IllegalStateException("Not authenticated"))

        messageApi.getChats(
            authorization = "Bearer $authToken"
        ).let { response ->
            if (response.isSuccessful) {
                response.body() ?: emptyList()
            } else {
                throw HttpException(response)
            }
        }
    }

//    fun observeMessagesFB(chatId: String): Flow<List<MessageDto>> = callbackFlow {
//        val database = Firebase.database
//        val ref = database.getReference("chats/$chatId/messages")
//
//        val listener = ref.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                val messages = snapshot.children.mapNotNull {
//                    it.getValue(MessageDto::class.java)
//                }
//                trySend(messages)
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                close(error.toException())
//            }
//        })
//
//        awaitClose {
//            ref.removeEventListener(listener)
//        }
//    }.catch { e ->
//        // Firebase automatically handles reconnections
//        emit(emptyList())
//    }

    private val WS_URL = "wss://your-api.com/ws"

    fun observeMessagesWS(chatId: String): Flow<List<MessageDto>> = callbackFlow {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("$WS_URL?chatId=$chatId")
            .addHeader("Authorization", "Bearer ${secureStorage.getAuthToken()}")
            .build()

        val webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onMessage(webSocket: WebSocket, text: String) {
                try {
                    val messages = emptyList<MessageDto>()//Json.decodeFromString<List<MessageDto>>(text)
                    trySend(messages)
                } catch (e: Exception) {
                    close(e)
                }
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                close(t)
            }
        })

        awaitClose {
            webSocket.close(1000, "Closing")
            client.dispatcher.executorService.shutdown()
        }
    }.catch { e ->
        // Handle errors and optionally reconnect
        emit(emptyList())
    }


}
