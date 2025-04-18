package com.example.messageapp.data.repository

import com.example.messageapp.data.datasource.remote.AuthApi
import com.example.messageapp.domain.security.SecureStorage
import com.example.messageapp.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow


class AuthRepositoryImpl(
    val authApi: AuthApi,
    val secureStorage: SecureStorage
) : AuthRepository {
    override suspend fun login(username: String, password: String): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun register(username: String, password: String): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun logout(): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun getToken(): Result<String> {
        TODO("Not yet implemented")
    }

    override fun observeAuthState(): Flow<Boolean> {
        TODO("Not yet implemented")
    }
}
