package com.example.trainmaintenancetracker.di

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import androidx.room.Room
import com.example.trainmaintenancetracker.data.datasource.local.RoomTaskDataSourceImpl
import com.example.trainmaintenancetracker.data.datasource.local.TrainMaintenanceDatabase
import com.example.trainmaintenancetracker.data.datasource.remote.RemoteTaskDataSourceImpl
import com.example.trainmaintenancetracker.data.datasource.remote.TaskApiService
import com.example.trainmaintenancetracker.data.datasource.remote.TrainMaintenanceNetworkClient.apiService
import com.example.trainmaintenancetracker.data.repository.ConnectivityRepositoryImpl
import com.example.trainmaintenancetracker.data.repository.TaskRepositoryImpl
import com.example.trainmaintenancetracker.data.sync.TaskSyncManager
import com.example.trainmaintenancetracker.data.sync.TaskSyncManagerImpl
import com.example.trainmaintenancetracker.domain.data.datasource.local.LocalTaskDataSource
import com.example.trainmaintenancetracker.domain.data.datasource.remote.RemoteTaskDataSource
import com.example.trainmaintenancetracker.domain.data.repository.ConnectivityRepository
import com.example.trainmaintenancetracker.domain.data.repository.TaskRepository
import com.example.trainmaintenancetracker.ui.screen.taskdetail.TaskDetailViewModel
import com.example.trainmaintenancetracker.ui.screen.tasks.TaskViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    // this scope is connected to the app lifecycle and will be cancelled when the app is closed
    // I'm injecting it therefore into the ConnectivityRepository and TaskSyncManager
    single { CoroutineScope(SupervisorJob() + Dispatchers.Default) }

    // DB
    single {
        Room.databaseBuilder(get<Application>(), TrainMaintenanceDatabase::class.java, "tasks_db")
            .fallbackToDestructiveMigration()
            .build()
    }

    //DAO
    single { get<TrainMaintenanceDatabase>().taskDao() }

    // ConnectivityManager
    single { androidApplication().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager }

    // repository
    single<ConnectivityRepository> { ConnectivityRepositoryImpl(get(), get()) }
    single<TaskRepository> { TaskRepositoryImpl(get()) }

    single<TaskSyncManager> { TaskSyncManagerImpl(get(), get(), get(), get()) }

    single<TaskApiService> { apiService }

    // Datasource
    single<LocalTaskDataSource> { RoomTaskDataSourceImpl(get()) }
    single<RemoteTaskDataSource> { RemoteTaskDataSourceImpl(get()) }

    viewModel { TaskViewModel(get(), get(), get()) }
    viewModel { TaskDetailViewModel(get(), get()) }
}
