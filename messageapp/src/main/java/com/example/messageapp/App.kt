package com.example.messageapp

import android.app.Application
import com.example.messageapp.di.appModule
import com.example.messageapp.domain.sync.MessageSyncManager
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        // Initialize Koin or any other dependency injection framework here

        startKoin {
            androidContext(this@App)
            modules(appModule)
        }

    }
}
