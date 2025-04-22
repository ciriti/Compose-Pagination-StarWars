package com.example.trainmaintenancetracker.ui.navigation

sealed class Route(val route: String) {
    data object Tasks : Route("tasks")
    data object TaskDetail : Route("task_detail") {
        fun taskDetail(taskId: String): String {
            return "task_detail/$taskId"
        }
    }
}
