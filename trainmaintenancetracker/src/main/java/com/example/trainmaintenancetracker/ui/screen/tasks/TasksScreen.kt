package com.example.trainmaintenancetracker.ui.screen.tasks

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.trainmaintenancetracker.R
import com.example.trainmaintenancetracker.domain.model.Task
import com.example.trainmaintenancetracker.ui.component.ErrorContent
import com.example.trainmaintenancetracker.ui.component.LoadingIndicator
import com.example.trainmaintenancetracker.ui.component.OfflineBanner
import kotlinx.collections.immutable.toImmutableList


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreenContent(
    state: TasksState,
    onTaskSelected: (String) -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {
        TopAppBar(title = { Text(stringResource(R.string.maintenance_tasks)) })
    },
    offlineBanner: @Composable (Boolean) -> Unit = { isOffline ->
        AnimatedVisibility(
            visible = !isOffline,
            enter = slideInVertically { -it } + fadeIn(),
            exit = slideOutVertically { -it } + fadeOut(),
            modifier = Modifier.fillMaxWidth()
        ) {
            OfflineBanner(
                text = stringResource(R.string.offline_mode_data_may_not_be_up_to_date),
                modifier = Modifier.fillMaxWidth()
            )
        }
    },
    taskItem: @Composable (Task, () -> Unit) -> Unit = { task, onClick ->
        TaskItem(task = task, onClick = onClick)
    },
    emptyContent: @Composable (Boolean, () -> Unit) -> Unit = { isConnected, onRefresh ->
        EmptyContent(
            isConnected = isConnected,
            onRefresh = onRefresh,
            modifier = modifier
        )
    }
) {
    Scaffold(
        topBar = topBar,
        modifier = modifier
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            when {
                state.isLoading -> LoadingIndicator()
                state.error != null -> ErrorContent(
                    errorMessage = state.error,
                    onRetry = onRefresh
                )

                state.tasks.isEmpty() -> emptyContent(state.isConnected, onRefresh)
                else -> TaskListContent(
                    tasks = state.tasks,
                    isConnected = state.isConnected,
                    onTaskClick = onTaskSelected,
                    offlineBanner = offlineBanner,
                    taskItem = taskItem
                )
            }
        }
    }
}

@Composable
fun TaskListContent(
    tasks: List<Task>,
    isConnected: Boolean,
    onTaskClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState(),
    offlineBanner: @Composable (Boolean) -> Unit,
    taskItem: @Composable (Task, () -> Unit) -> Unit
) {
    Column(modifier = modifier) {
        offlineBanner(isConnected)

        LazyColumn(state = listState) {
            items(items = tasks, key = { it.taskId }) { task ->
                taskItem(task) { onTaskClick(task.taskId) }
            }
        }
    }
}

@Composable
fun EmptyContent(
    isConnected: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(R.string.no_tasks_available))
        if (!isConnected) {
            Text(text = stringResource(R.string.connect_to_sync_tasks))
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRefresh) {
            Text(text = stringResource(R.string.refresh))
        }
    }
}

@Preview(name = "SuccessCase")
@Composable
fun TaskScreenSuccessPreview() {
    TaskScreenContent(
        state = TasksState(tasks = mockTasks.toImmutableList()),
        onTaskSelected = {},
        onRefresh = {}
    )
}

@Preview(name = "SuccessCaseEmptyList")
@Composable
fun TaskScreenSuccessEmptyListPreview() {
    TaskScreenContent(
        state = TasksState(tasks = emptyList<Task>().toImmutableList()),
        onTaskSelected = {},
        onRefresh = {}
    )
}

@Preview(name = "LoadingCase")
@Composable
fun TaskScreenLoadingPreview() {
    TaskScreenContent(
        state = TasksState(isLoading = true),
        onTaskSelected = {},
        onRefresh = {}
    )
}

@Preview(name = "ErrorCase")
@Composable
fun TaskScreenErrorPreview() {
    TaskScreenContent(
        state = TasksState(error = "Error loading tasks"),
        onTaskSelected = {},
        onRefresh = {}
    )
}

private val mockTasks = listOf(
    Task(
        taskId = "1",
        trainId = "TR-123",
        taskType = "Inspection",
        priorityLevel = "High",
        location = "Depot A",
        dueDate = "2023-12-01",
        description = "Full inspection required"
    ),
    // Add more mock tasks as needed
)
