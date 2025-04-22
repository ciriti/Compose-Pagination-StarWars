package com.example.trainmaintenancetracker.ui.screen.taskdetail

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.trainmaintenancetracker.ui.navigation.Route

fun NavGraphBuilder.taskDetailRoute(navController: NavHostController) {
    composable(Route.TaskDetail.route) {
        val taskId = it.arguments?.getString("taskId") ?: ""
        TaskDetailRoute(taskId = taskId, onBackClick = { navController.popBackStack() })
    }
}
