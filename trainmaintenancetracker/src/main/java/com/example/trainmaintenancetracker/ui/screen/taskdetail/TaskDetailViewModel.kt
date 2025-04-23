package com.example.trainmaintenancetracker.ui.screen.taskdetail

import androidx.lifecycle.viewModelScope
import com.example.trainmaintenancetracker.domain.data.repository.ConnectivityRepository
import com.example.trainmaintenancetracker.domain.data.repository.TaskRepository
import com.example.trainmaintenancetracker.ui.component.BaseViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class TaskDetailViewModel(
    private val taskRepository: TaskRepository,
    connectivityRepository: ConnectivityRepository,
) : BaseViewModel<TaskDetailIntent, TaskDetailState, TaskDetailEffect>() {

    init {
        connectivityRepository.isConnected
            .onEach { isConnected ->
                setState { copy(isConnected = isConnected) }
            }
            .launchIn(viewModelScope)
    }

    override fun createInitialState(): TaskDetailState = TaskDetailState()

    override fun handleIntent(intent: TaskDetailIntent) {
        when (intent) {
            is TaskDetailIntent.LoadTask -> loadTask(intent.taskId)
        }
    }

    private fun loadTask(taskId: String) {
        viewModelScope.launch {
            setState { copy(isLoading = true) }
            taskRepository.getTaskById(taskId)
                .onSuccess { newTask ->
                    if (newTask != state.value.task) {
                        setState { copy(task = newTask, isLoading = false) }
                    } else {
                        setState { copy(isLoading = false) }
                    }
                }
                .onFailure { error ->
                    setState { copy(isLoading = false, error = error.message) }
                    setEffect { TaskDetailEffect.ShowError(error.message ?: "Failed to load task") }
                }
        }
    }


}
