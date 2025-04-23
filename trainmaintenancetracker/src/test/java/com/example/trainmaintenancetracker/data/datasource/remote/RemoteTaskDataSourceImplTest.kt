package com.example.trainmaintenancetracker.data.datasource.remote

import com.example.trainmaintenancetracker.data.datasource.remote.model.TaskResponse
import com.example.trainmaintenancetracker.data.testTaskDtos
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class RemoteTaskDataSourceImplTest {

    private lateinit var dataSource: RemoteTaskDataSourceImpl
    private val taskApiService: TaskApiService = mockk()

    @Before
    fun setUp() {
        dataSource = RemoteTaskDataSourceImpl(
            taskApiService = taskApiService,
            exceptionMapper = { response -> Exception("HTTP ${response.code()}") }
        )
    }

    @Test
    fun `getAllTasks should return tasks when successful`() = runBlocking {
        // Arrange
        val testResponse = mockk<Response<TaskResponse>> {
            coEvery { isSuccessful } returns true
            coEvery { body() } returns TaskResponse(testTaskDtos)
        }
        coEvery { taskApiService.getAllTasks() } returns testResponse

        // Act
        val result = dataSource.getAllTasks()

        // Assert
        assertEquals(true, result.isSuccess)
        assertEquals(2, result.getOrNull()?.size)
        assertEquals("1", result.getOrNull()?.get(0)?.taskId)
        coVerify { taskApiService.getAllTasks() }
    }

    @Test
    fun `getAllTasks should propagate exceptions`() = runBlocking {
        // Arrange
        val testError = Exception("Network error")
        coEvery { taskApiService.getAllTasks() } throws testError

        // Act
        val result = dataSource.getAllTasks()

        // Assert
        assertEquals(true, result.isFailure)
        assertEquals("Network error", result.exceptionOrNull()?.message)
    }
}
