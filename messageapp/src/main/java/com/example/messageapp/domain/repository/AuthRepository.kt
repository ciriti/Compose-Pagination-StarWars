package com.example.messageapp.domain.repository

import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(username: String, password: String): Result<Unit>
    suspend fun register(username: String, password: String): Result<Unit>
    suspend fun logout(): Result<Unit>
    suspend fun getToken(): Result<String>
    fun observeAuthState(): Flow<Boolean>
}
