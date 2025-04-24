package com.example.brochures.ui.screen.brochure

import app.cash.turbine.test
import com.example.brochures.domain.datasource.repository.BrochureRepository
import com.example.brochures.domain.mapper.toBrochure
import com.example.brochures.testBrochure1
import com.example.brochures.testBrochure2
import com.example.brochures.testBrochures
import com.example.brochures.validContentDtos
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BrochureViewModelTest {

    private val mockRepository: BrochureRepository = mockk(relaxed = true)
    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state should be loading and then show brochures when successful`() = testScope.runTest {
        // Arrange
        coEvery { mockRepository.getBrochures() } returns Result.success(testBrochures)

        // Act
        val viewModel = BrochureViewModel(mockRepository)

        // Assert
        viewModel.state.test {
            assertEquals(BrochureState(isLoading = false), awaitItem())
            assertEquals(BrochureState(isLoading = true), awaitItem())

            val successState = awaitItem()
            assertEquals(false, successState.isLoading)
            assertEquals(testBrochures.size, successState.brochures.size)
            assertEquals("1", successState.brochures[0].id)
        }
    }

    @Test
    fun `should show error state when repository fails`() = testScope.runTest {
        // Arrange
        val errorMessage = "Unknown error"
        coEvery { mockRepository.getBrochures() } returns Result.failure(Exception(errorMessage))

        // Act
        val viewModel = BrochureViewModel(mockRepository)

        // Assert
        viewModel.state.test {
            assertEquals(BrochureState(isLoading = false), awaitItem())
            assertEquals(BrochureState(isLoading = true), awaitItem())

            val errorState = awaitItem()
            assertEquals(false, errorState.isLoading)
            assertEquals(errorMessage, errorState.error)
        }
    }

    @Test
    fun `filterByDistance should update state with filtered brochures`() = testScope.runTest {
        // Arrange
        val testBrochures = listOf(testBrochure1, testBrochure2).toImmutableList()
        coEvery { mockRepository.getBrochures() } returns Result.success(testBrochures)

        val viewModel = BrochureViewModel(mockRepository)

        viewModel.state.test {
            awaitItem() // Initial state
            awaitItem() // Loading state
            awaitItem() // Success state
        }

        // Act
        viewModel.handleIntent(BrochureIntent.FilterByDistance(true))

        // Assert
        viewModel.state.test {
            val filteredState = awaitItem()
            assertEquals(true, filteredState.filterByDistance)
            assertEquals(1, filteredState.brochures.size)
            assertEquals("1", filteredState.brochures[0].id)
        }
    }

    @Test
    fun `filterByDistance should show all brochures when disabled`() = testScope.runTest {
        // Arrange
        val testBrochures = validContentDtos.map { it.toBrochure() }.toImmutableList()
        coEvery { mockRepository.getBrochures() } returns Result.success(testBrochures)

        val viewModel = BrochureViewModel(mockRepository)

        viewModel.state.test {
            awaitItem() // Initial state
            awaitItem() // Loading state
            awaitItem() // Success state
        }

        viewModel.handleIntent(BrochureIntent.FilterByDistance(true))

        // Act
        viewModel.handleIntent(BrochureIntent.FilterByDistance(false))

        // Assert
        viewModel.state.test {
            val unfilteredState = awaitItem()
            assertEquals(false, unfilteredState.filterByDistance)
            assertEquals(testBrochures.size, unfilteredState.brochures.size)
        }
    }
}
