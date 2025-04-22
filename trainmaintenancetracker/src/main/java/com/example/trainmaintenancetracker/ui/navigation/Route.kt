package com.example.trainmaintenancetracker.ui.navigation

sealed class Route(val route: String) {
    data object Tasks : Route("tasks")
    data object TaskDetail : Route("task_detail/{taskId}") {
        fun createRoute(taskId: String) = "task_detail/$taskId"
    }
}
