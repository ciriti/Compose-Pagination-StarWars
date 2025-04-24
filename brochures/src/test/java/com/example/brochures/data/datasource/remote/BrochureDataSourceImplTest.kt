package com.example.brochures.data.datasource.remote

import com.example.brochures.testBrochureContent
import com.example.brochures.testContentDtos
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
import retrofit2.Response

class BrochureDataSourceImplTest {

    private lateinit var dataSource: BrochureDataSourceImpl
    private val brochureApi: BrochureApi = mockk()
    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        dataSource = BrochureDataSourceImpl(
            api = brochureApi,
            ioDispatcher = testDispatcher,
            exceptionMapper = { response -> Exception("HTTP ${response.code()}") }
        )
    }

    @Test
    fun `fetchBrochures should return filtered brochures when successful`() = runBlocking {
        // Arrange
        val testResponse = mockk<Response<BrochureResponse>> {
            coEvery { isSuccessful } returns true
            coEvery { body() } returns BrochureResponse(
                EmbeddedContentsDto(testContentDtos)
            )
        }
        coEvery { brochureApi.getBrochures() } returns testResponse

        // Act
        val result = dataSource.fetchBrochures()

        // Assert
        assertTrue(result.isSuccess)
        assertEquals(2, result.getOrNull()?.size)
        assertEquals("123", result.getOrNull()?.get(0)?.content?.id)
        coVerify { brochureApi.getBrochures() }
    }

    @Test
    fun `fetchBrochures should filter out null imageUrls`() = runBlocking {
        // Arrange
        val testResponse = mockk<Response<BrochureResponse>> {
            coEvery { isSuccessful } returns true
            coEvery { body() } returns BrochureResponse(
                EmbeddedContentsDto(
                    testContentDtos + ContentDto(
                        "brochure",
                        testBrochureContent.copy(imageUrl = null)
                    )
                )
            )
        }
        coEvery { brochureApi.getBrochures() } returns testResponse

        // Act
        val result = dataSource.fetchBrochures()

        // Assert
        assertTrue(result.isSuccess)
        assertEquals(2, result.getOrNull()?.size)
    }

    @Test
    fun `fetchBrochures should propagate exceptions`() = runBlocking {
        // Arrange
        val testError = Exception("Network error")
        coEvery { brochureApi.getBrochures() } throws testError

        // Act
        val result = dataSource.fetchBrochures()

        // Assert
        assertTrue(result.isFailure)
        assertEquals("Network error", result.exceptionOrNull()?.message)
    }

    @Test
    fun `fetchBrochures should throw when empty response`() = runBlocking {
        // Arrange
        val testResponse = mockk<Response<BrochureResponse>> {
            coEvery { isSuccessful } returns true
            coEvery { body() } returns null
        }
        coEvery { brochureApi.getBrochures() } returns testResponse

        // Act
        val result = dataSource.fetchBrochures()

        // Assert
        assertTrue(result.isFailure)
        assertEquals("Empty response", result.exceptionOrNull()?.message)
    }
}
