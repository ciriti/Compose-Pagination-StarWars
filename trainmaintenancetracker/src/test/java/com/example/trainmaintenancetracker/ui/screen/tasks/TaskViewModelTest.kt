package com.example.trainmaintenancetracker.ui.screen.tasks

import app.cash.turbine.test
import com.example.trainmaintenancetracker.data.sync.TaskSyncManager
import com.example.trainmaintenancetracker.data.testEntity1
import com.example.trainmaintenancetracker.domain.data.repository.ConnectivityRepository
import com.example.trainmaintenancetracker.domain.data.repository.TaskRepository
import com.example.trainmaintenancetracker.domain.mapper.toDomain
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class TaskViewModelTest {
    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    private val mockRepo: TaskRepository = mockk(relaxed = true)
    private val mockConnectivity: ConnectivityRepository = mockk(relaxed = true)
    private val mockSync: TaskSyncManager = mockk(relaxed = true)

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `should show loading state initially`() = testScope.runTest {
        // Arrange
        coEvery { mockRepo.observeAllTasks() } returns flowOf(listOf(testEntity1.toDomain()))
        coEvery { mockConnectivity.isConnected } returns MutableStateFlow(true)
        coEvery { mockSync.triggerImmediateSync() } returns Unit

        // Act
        val viewModel = TaskViewModel(
            mockRepo,
            mockConnectivity,
            mockSync
        )

        // Assert
        viewModel.state.test {
            assertEquals(false, awaitItem().isLoading)
            assertEquals(true, awaitItem().isLoading)
            awaitItem().also {
                assertEquals(false, it.isLoading)
                assertEquals(listOf(testEntity1.toDomain()), it.tasks)
            }
        }
    }

    @Test
    fun `should handle error state when task observation fails`() = testScope.runTest {
        // Arrange
        val errorMessage = "Failed to load tasks"
        coEvery { mockRepo.observeAllTasks() } returns flow { throw RuntimeException(errorMessage) }
        coEvery { mockConnectivity.isConnected } returns MutableStateFlow(false)
        coEvery { mockSync.triggerImmediateSync() } returns Unit

        // Act
        val viewModel = TaskViewModel(
            mockRepo,
            mockConnectivity,
            mockSync
        )

        // Assert
        viewModel.state.test {
            assertEquals(false, awaitItem().isLoading)
            assertEquals(true, awaitItem().isLoading)
            val errorState = awaitItem()
            assertEquals(false, errorState.isLoading)
            assertEquals(errorMessage, errorState.error)
        }
    }

    @Test
    fun `should update connectivity state when network status changes`() = testScope.runTest {
        // Arrange
        val connectivityFlow = MutableStateFlow(false)
        coEvery { mockRepo.observeAllTasks() } returns flowOf(emptyList())
        coEvery { mockConnectivity.isConnected } returns connectivityFlow
        coEvery { mockSync.triggerImmediateSync() } returns Unit

        // Act
        val viewModel = TaskViewModel(
            mockRepo,
            mockConnectivity,
            mockSync
        )

        // Assert initial state
        viewModel.state.test {
            assertEquals(false, awaitItem().isLoading)
            assertEquals(true, awaitItem().isLoading) // Loading
            assertEquals(false, awaitItem().isConnected) // Initial disconnected state

            // Change connectivity
            connectivityFlow.value = true

            // Verify update
            val connectedState = awaitItem()
            assertEquals(true, connectedState.isConnected)
            assertEquals(false, connectedState.isLoading)
        }
    }

}
