package com.example.trainmaintenancetracker.ui.screen.taskdetail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun TaskDetailRoute(
    taskId: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TaskDetailViewModel = koinViewModel(parameters = { parametersOf(taskId) })
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(taskId) {
        viewModel.processIntent(TaskDetailIntent.LoadTask(taskId))
    }

    TaskDetailScreen(
        state = state,
        onBackClick = onBackClick,
        modifier = modifier,
        onRetry = { viewModel.processIntent(TaskDetailIntent.LoadTask(taskId)) }
    )
}
