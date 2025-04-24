package com.example.brochures.data.repository

import com.example.brochures.data.datasource.remote.ContentDto
import com.example.brochures.testBrochureContent
import com.example.brochures.testContentDtos
import com.example.brochures.domain.datasource.remote.BrochureDataSource
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class BrochureRepositoryImplTest {

    private lateinit var repository: BrochureRepositoryImpl
    private val dataSource: BrochureDataSource = mockk()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        repository = BrochureRepositoryImpl(
            remoteDataSource = dataSource,
            ioDispatcher = testDispatcher
        )
    }

    @Test
    fun `getBrochures should return immutable list of brochures when successful`() = runBlocking {
        // Arrange
        coEvery { dataSource.fetchBrochures() } returns Result.success(testContentDtos)

        // Act
        val result = repository.getBrochures()

        // Assert
        assertTrue(result.isSuccess)
        val brochures = result.getOrNull()!!
        assertEquals(2, brochures.size)
        assertEquals("123", brochures[0].id)
        assertEquals("Test Retailer", brochures[0].retailer)
        coVerify { dataSource.fetchBrochures() }
    }

    @Test
    fun `getBrochures should propagate data source errors`() = runBlocking {
        // Arrange
        val testError = Exception("Network error")
        coEvery { dataSource.fetchBrochures() } returns Result.failure(testError)

        // Act
        val result = repository.getBrochures()

        // Assert
        assertTrue(result.isFailure)
        assertEquals("Network error", result.exceptionOrNull()?.message)
    }

    @Test
    fun `getBrochures should filter out invalid content`() = runBlocking {
        // Arrange
        val mixedContent = testContentDtos + listOf(
            ContentDto("invalidType", null),
            ContentDto("brochure", testBrochureContent.copy(id = null))
        )
        coEvery { dataSource.fetchBrochures() } returns Result.success(mixedContent)

        // Act
        val result = repository.getBrochures()

        // Assert
        assertTrue(result.isSuccess)
        assertEquals(2, result.getOrNull()?.size)
    }

    @Test
    fun `getBrochures should run on IO dispatcher`() = runBlocking {
        // Arrange
        coEvery { dataSource.fetchBrochures() } returns Result.success(testContentDtos)

        // Act
        repository.getBrochures()

        // Assert
        coVerify { dataSource.fetchBrochures() }
    }
}
