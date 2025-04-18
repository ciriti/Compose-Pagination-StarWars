package com.example.messageapp.di

import android.content.Context
import android.net.ConnectivityManager
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.messageapp.data.network.ConnectivityObserverImpl
import com.example.messageapp.data.network.ConnectivityRepository
import com.example.messageapp.data.network.ConnectivityRepositoryImpl
import com.example.messageapp.data.repository.MessageRepositoryImpl
import com.example.messageapp.data.sync.MessageSyncManagerImpl
import com.example.messageapp.data.sync.MessageSyncWorker
import com.example.messageapp.domain.network.ConnectivityObserver
import com.example.messageapp.domain.repository.MessageRepository
import com.example.messageapp.domain.sync.MessageSyncManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val appModule = module {
    single { CoroutineScope(SupervisorJob() + Dispatchers.Default) }
    single { androidApplication().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager }
    single<ConnectivityObserver> { ConnectivityObserverImpl(get()) }
    single<ConnectivityRepository> { ConnectivityRepositoryImpl(get(), get(), get()) }
    single<MessageRepository> { MessageRepositoryImpl(get(), get()) }

    // Add worker factory
    factory { (context: Context, params: WorkerParameters) ->
        MessageSyncWorker(context, params)
    }

    // Simple worker factory
    factory { (context: Context, params: WorkerParameters) ->
        MessageSyncWorker(context, params)
    }

    single { WorkManager.getInstance(get()) }

    single<MessageSyncManager> { MessageSyncManagerImpl(get()) }
}
