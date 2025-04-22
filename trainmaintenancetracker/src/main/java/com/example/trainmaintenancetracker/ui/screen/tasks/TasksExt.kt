package com.example.trainmaintenancetracker.ui.screen.tasks

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.trainmaintenancetracker.ui.navigation.Route

fun NavGraphBuilder.tasksRoute(
    navController: NavHostController,
) {
    composable(Route.Tasks.route) {
        TaskRoute(
            onTaskSelected = { taskId ->
                navController.navigate(Route.TaskDetail.createRoute(taskId)){
                    popUpTo(Route.Tasks.route) {
                        saveState = true
                    }
                    restoreState = true
                    launchSingleTop = true
                }
            },
        )
    }
}
