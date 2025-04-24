package com.example.trainmaintenancetracker.ui.screen.tasks

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.example.trainmaintenancetracker.domain.model.Task
import com.example.trainmaintenancetracker.ui.component.OfflineBanner
import org.junit.Rule
import org.junit.Test

class TasksScreenKtTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun offlineBannerIsDisplayedWhenDisconnected() {
        // Arrange
        val testTasks = listOf(testTask1)

        // Act
        composeTestRule.setContent {
            TaskListContent(
                tasks = testTasks,
                isConnected = false,
                onTaskClick = {},
                offlineBanner = { isConnected ->
                    if (!isConnected) {
                        OfflineBanner(
                            text = "Offline mode - data may not be up to date",
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                taskItem = { task, onClick ->
                    Text(text = task.taskType)
                }
            )
        }

        // Assert
        composeTestRule.onNodeWithText("Offline mode - data may not be up to date")
            .assertExists()
    }

    @Test
    fun offlineBannerIsHiddenWhenConnected() {
        // Arrange
        val testTasks = listOf(testTask1)

        // Act
        composeTestRule.setContent {
            TaskListContent(
                tasks = testTasks,
                isConnected = true,
                onTaskClick = {},
                offlineBanner = { isConnected ->
                    if (!isConnected) {
                        OfflineBanner(
                            text = "Offline mode - data may not be up to date",
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                taskItem = { task, onClick ->
                    Text(text = task.taskType)
                }
            )
        }

        // Assert
        composeTestRule.onNodeWithText("Offline mode - data may not be up to date")
            .assertDoesNotExist()
    }

    @Test
    fun taskListDisplaysCorrectItems() {
        // Arrange
        val testTasks = listOf(testTask1, testData2)

        // Act
        composeTestRule.setContent {
            TaskListContent(
                tasks = testTasks,
                isConnected = true,
                onTaskClick = {},
                offlineBanner = { _ -> },
                taskItem = { task, _ ->
                    Text(text = "${task.taskType} - ${task.location}")
                }
            )
        }

        // Assert
        composeTestRule.onNodeWithText("Inspection - Depot A").assertExists()
        composeTestRule.onNodeWithText("Maintenance - Depot B").assertExists()
    }

    @Test
    fun errorStateDisplaysErrorMessage() {
        // Arrange
        val errorMessage = "Failed to load tasks"

        // Act
        composeTestRule.setContent {
            TaskScreenContent(
                onTaskSelected = {},
                onRefresh = {},
                state = TasksState(error = errorMessage),
                offlineBanner = { _ -> },
                taskItem = { _, _ -> },
            )
        }

        // Assert
        composeTestRule.onNodeWithText(errorMessage).assertExists()
    }
}
