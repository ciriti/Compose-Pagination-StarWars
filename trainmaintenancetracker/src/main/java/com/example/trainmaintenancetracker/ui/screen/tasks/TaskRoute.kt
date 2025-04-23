package com.example.trainmaintenancetracker.ui.screen.tasks

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel

@Composable
fun TaskRoute(
    onTaskSelected: (String) -> Unit,
    viewModel: TaskViewModel = koinViewModel(),
    modifier: Modifier = Modifier.semantics { contentDescription = "Tasks" },
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.processIntent(TasksIntent.LoadTasks)
    }

    TaskScreenContent(
        state = state,
        onTaskSelected = onTaskSelected,
        onRefresh = { viewModel.processIntent(TasksIntent.RefreshTasks) },
        modifier = modifier
    )
}
