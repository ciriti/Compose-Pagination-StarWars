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
        val testTasks = listOf(
            Task(
                taskId = "1",
                trainId = "TR-123",
                taskType = "Inspection",
                priorityLevel = "High",
                location = "Depot A",
                dueDate = "2023-12-01",
                description = "Full inspection required"
            )
        )

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
        val testTasks = listOf(
            Task(
                taskId = "1",
                trainId = "TR-123",
                taskType = "Inspection",
                priorityLevel = "High",
                location = "Depot A",
                dueDate = "2023-12-01",
                description = "Full inspection required"
            )
        )

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
}
