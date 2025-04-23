package com.example.trainmaintenancetracker.data.sync

import com.example.trainmaintenancetracker.data.testTaskDtos
import com.example.trainmaintenancetracker.domain.data.datasource.local.LocalTaskDataSource
import com.example.trainmaintenancetracker.domain.data.datasource.remote.RemoteTaskDataSource
import com.example.trainmaintenancetracker.domain.data.repository.ConnectivityRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class TaskSyncManagerImplTest {

    private lateinit var syncManager: TaskSyncManagerImpl
    private val localDataSource: LocalTaskDataSource = mockk()
    private val remoteDataSource: RemoteTaskDataSource = mockk()
    private lateinit var connectivityRepository: ConnectivityRepository

    @Before
    fun setUp() {

        connectivityRepository = object : ConnectivityRepository {
            override val isConnected: StateFlow<Boolean>
                get() = MutableStateFlow(true)
        }

        syncManager = TaskSyncManagerImpl(
            localDataSource = localDataSource,
            remoteDataSource = remoteDataSource,
            connectivityRepository = connectivityRepository,
            coroutineScope = CoroutineScope(Dispatchers.Unconfined)
        )
    }

    @Test
    fun `syncTasks should insert tasks when remote fetch succeeds`() = runBlocking {
        // Arrange
        val remoteTasks = testTaskDtos
        coEvery { remoteDataSource.getAllTasks() } returns Result.success(remoteTasks)
        coEvery { localDataSource.insertTasks(any()) } returns Unit

        // Act
        val result = syncManager.triggerImmediateSync()

        // Assert
        coVerify(exactly = 1) {
            localDataSource.insertTasks(match { it.size == remoteTasks.size })
        }
    }

    @Test
    fun `syncTasks should return error when remote fetch fails`() = runBlocking {
        // Arrange
        val testError = Exception("Network error")
        coEvery { remoteDataSource.getAllTasks() } returns Result.failure(testError)

        // Act
        val result = syncManager.triggerImmediateSync()

        // Assert
        coVerify(exactly = 0) { localDataSource.insertTasks(any()) }
    }
}
