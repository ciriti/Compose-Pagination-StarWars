package com.example.trainmaintenancetracker.ui.screen.tasks

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.example.trainmaintenancetracker.domain.model.Task
import com.example.trainmaintenancetracker.ui.component.UiEffect
import com.example.trainmaintenancetracker.ui.component.UiIntent
import com.example.trainmaintenancetracker.ui.component.UiState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

// Intents
sealed class TasksIntent : UiIntent {
    data object LoadTasks : TasksIntent()
    data object RefreshTasks : TasksIntent()
}

// State
@Immutable
data class TasksState(
    val tasks: ImmutableList<Task> = persistentListOf(),
    val isLoading: Boolean = false,
    val isConnected: Boolean = false,
    val error: String? = null,
) : UiState

// Effects
sealed class TasksEffect : UiEffect {
    data class ShowError(val message: String) : TasksEffect()
}
