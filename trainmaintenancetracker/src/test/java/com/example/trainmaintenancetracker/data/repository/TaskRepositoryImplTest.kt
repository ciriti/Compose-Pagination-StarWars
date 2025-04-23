package com.example.trainmaintenancetracker.data.repository

import com.example.trainmaintenancetracker.data.datasource.local.TaskEntity
import com.example.trainmaintenancetracker.data.testEntities
import com.example.trainmaintenancetracker.data.testEntity1
import com.example.trainmaintenancetracker.domain.data.datasource.local.LocalTaskDataSource
import com.example.trainmaintenancetracker.domain.model.Task
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class TaskRepositoryImplTest {

    private lateinit var repository: TaskRepositoryImpl
    private val localDataSource: LocalTaskDataSource = mockk()
    private val testDispatcher = Dispatchers.Unconfined

    @Before
    fun setUp() {
        repository = TaskRepositoryImpl(
            localDataSource = localDataSource,
            ioDispatcher = testDispatcher
        )
    }

    @Test
    fun `observeAllTasks should return mapped tasks`() = runBlocking {
        // Arrange
        coEvery { localDataSource.observeAllTasks() } returns flowOf(testEntities)

        // Act
        val resultFlow = repository.observeAllTasks()
        val results = mutableListOf<List<Task>>()
        resultFlow.collect { results.add(it) }

        // Assert
        assertEquals(1, results.size)
        assertEquals(2, results[0].size)
        assertEquals("1", results[0][0].taskId)
        assertEquals("train1", results[0][0].trainId)
        coVerify { localDataSource.observeAllTasks() }
    }

    @Test
    fun `getTasks should return mapped tasks`() = runBlocking {
        // Arrange
        val testEntities = listOf(testEntity1)
        coEvery { localDataSource.getTasks() } returns Result.success(testEntities)

        // Act
        val result = repository.getTasks()

        // Assert
        assertEquals(true, result.isSuccess)
        assertEquals(1, result.getOrNull()?.size)
        assertEquals("train1", result.getOrNull()?.get(0)?.trainId)
        coVerify { localDataSource.getTasks() }
    }

    @Test
    fun `getTasks should propagate error`() = runBlocking {
        // Arrange
        val testError = Exception("Database error")
        coEvery { localDataSource.getTasks() } returns Result.failure(testError)

        // Act
        val result = repository.getTasks()

        // Assert
        assertEquals(true, result.isFailure)
        assertEquals("Database error", result.exceptionOrNull()?.message)
    }

    @Test
    fun `getTaskById should return mapped task`() = runBlocking {
        // Arrange
        val testEntity =
            TaskEntity("1", "train1", "Brake", "High", "Location1", "2023-01-01", "Desc1")
        coEvery { localDataSource.getTaskById("1") } returns Result.success(testEntity)

        // Act
        val result = repository.getTaskById("1")

        // Assert
        assertEquals(true, result.isSuccess)
        assertEquals("train1", result.getOrNull()?.trainId)
        coVerify { localDataSource.getTaskById("1") }
    }

    @Test
    fun `getTaskById should propagate error`() = runBlocking {
        // Arrange
        val testError = Exception("Task not found")
        coEvery { localDataSource.getTaskById("99") } returns Result.failure(testError)

        // Act
        val result = repository.getTaskById("99")

        // Assert
        assertEquals(true, result.isFailure)
        assertEquals("Task not found", result.exceptionOrNull()?.message)
    }
}
