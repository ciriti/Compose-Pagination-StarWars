package com.example.trainmaintenancetracker.ui.screen.taskdetail

import com.example.trainmaintenancetracker.domain.model.Task
import com.example.trainmaintenancetracker.ui.component.UiEffect
import com.example.trainmaintenancetracker.ui.component.UiIntent
import com.example.trainmaintenancetracker.ui.component.UiState

// Intents
sealed class TaskDetailIntent : UiIntent {
    data class LoadTask(val taskId: String) : TaskDetailIntent()
}

// State
data class TaskDetailState(
    val task: Task? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isConnected: Boolean = false,
) : UiState

// Effects
sealed class TaskDetailEffect : UiEffect {
    data class ShowError(val message: String) : TaskDetailEffect()
}
