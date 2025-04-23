package com.example.trainmaintenancetracker.ui.screen.tasks

import androidx.lifecycle.viewModelScope
import com.example.trainmaintenancetracker.data.sync.TaskSyncManager
import com.example.trainmaintenancetracker.domain.data.repository.ConnectivityRepository
import com.example.trainmaintenancetracker.domain.data.repository.TaskRepository
import com.example.trainmaintenancetracker.ui.component.BaseViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class TaskViewModel(
    private val taskRepository: TaskRepository,
    private val connectivityRepository: ConnectivityRepository,
    private val syncManager: TaskSyncManager,
) : BaseViewModel<TasksIntent, TasksState, TasksEffect>() {

    init {

        viewModelScope.launch {
            syncManager.triggerImmediateSync()
        }

        // Combine task state and connectivity state
        viewModelScope.launch {
            taskRepository.observeAllTasks()
                .combine(connectivityRepository.isConnected) { tasks, isConnected ->
                    tasks to isConnected
                }
                .onStart { setState { copy(isLoading = true) } }
                .catch { error ->
                    setState { copy(isLoading = false, error = error.message) }
                    setEffect { TasksEffect.ShowError(error.message ?: "Unknown error") }
                }
                .collect { (newTasks, isConnected) ->
                    setState {
                        copy(
                            isLoading = false,
                            error = null,
                            tasks = newTasks.toImmutableList(),
                            isConnected = isConnected
                        )
                    }
                }
        }
    }

    override fun createInitialState(): TasksState = TasksState()

    override fun handleIntent(intent: TasksIntent) {
        when (intent) {
            is TasksIntent.LoadTasks -> loadTasks()
            is TasksIntent.RefreshTasks -> refreshTasks()
        }
    }

    private fun refreshTasks() {
        viewModelScope.launch {
            syncManager.triggerImmediateSync()
        }
    }

    private fun loadTasks() {
        viewModelScope.launch {
            taskRepository.getTasks()
                .onSuccess {
                    setState {
                        copy(
                            isLoading = false,
                            error = null,
                            tasks = it.toImmutableList(),
                        )
                    }
                }
                .onFailure { error ->
                    setState { copy(isLoading = false, error = error.message) }
                    setEffect { TasksEffect.ShowError(error.message ?: "Unknown error") }
                }
        }

    }
}
