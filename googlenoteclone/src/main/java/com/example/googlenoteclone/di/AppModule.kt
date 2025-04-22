package com.example.googlenoteclone.di

import com.example.googlenoteclone.data.repository.ConnectivityRepository
import com.example.googlenoteclone.data.repository.ConnectivityRepositoryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.dsl.module
import kotlin.coroutines.CoroutineContext

val appModule = module {
    // Default dispatcher tied to application lifecycle
    single<CoroutineContext> { Dispatchers.Default }

    // OR more commonly, tied to application lifecycle:
    single<CoroutineScope> { CoroutineScope(SupervisorJob() + Dispatchers.IO) }
    single<CoroutineContext> { get<CoroutineScope>().coroutineContext }

    single<ConnectivityRepository> { ConnectivityRepositoryImpl(get(), get(), get()) }
}
