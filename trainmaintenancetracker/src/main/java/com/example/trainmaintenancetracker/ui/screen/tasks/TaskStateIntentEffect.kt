package com.example.trainmaintenancetracker.ui.screen.tasks

import com.example.trainmaintenancetracker.domain.model.Task
import com.example.trainmaintenancetracker.ui.component.UiEffect
import com.example.trainmaintenancetracker.ui.component.UiIntent
import com.example.trainmaintenancetracker.ui.component.UiState

// Intents
sealed class TasksIntent : UiIntent {
    data object LoadTasks : TasksIntent()
    data object RefreshTasks : TasksIntent()
}

// State
data class TasksState(
    val tasks: List<Task> = emptyList(),
    val isLoading: Boolean = false,
    val isConnected: Boolean = false,
    val error: String? = null,
) : UiState

// Effects
sealed class TasksEffect : UiEffect {
    data class ShowError(val message: String) : TasksEffect()
}
