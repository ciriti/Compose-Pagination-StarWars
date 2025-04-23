package com.example.trainmaintenancetracker.ui.screen.taskdetail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.trainmaintenancetracker.R
import com.example.trainmaintenancetracker.domain.model.Task
import com.example.trainmaintenancetracker.domain.model.isNotEmpty
import com.example.trainmaintenancetracker.ui.component.ErrorContent
import com.example.trainmaintenancetracker.ui.component.LoadingIndicator
import com.example.trainmaintenancetracker.ui.component.OfflineBanner

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailScreen(
    state: TaskDetailState,
    onBackClick: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.task_details)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                }
            )
        },
        modifier = modifier
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            AnimatedVisibility(
                visible = !state.isConnected,
                enter = fadeIn() + slideInVertically { -it },
                exit = fadeOut() + slideOutVertically { -it },
                modifier = Modifier.fillMaxWidth()
            ) {
                OfflineBanner(
                    text = stringResource(R.string.offline_mode_data_may_not_be_up_to_date),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            when {
                state.isLoading -> LoadingIndicator()
                state.error != null -> ErrorContent(errorMessage = state.error, onRetry = onRetry)
                state.task.isNotEmpty -> TaskDetailContent(
                    task = state.task,
                    modifier = Modifier.fillMaxWidth()
                )
                else -> Text("No task loaded")
            }
        }
    }
}

@Composable
private fun TaskDetailContent(
    task: Task,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = task.taskType,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))

                DetailRow(label = stringResource(R.string.train_detail), value = task.trainId)
                DetailRow(label = stringResource(R.string.priority_detail), value = task.priorityLevel)
                DetailRow(label = stringResource(R.string.location), value = task.location)
                DetailRow(label = stringResource(R.string.due_date), value = task.dueDate)

                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.description),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = task.description,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(vertical = 4.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
