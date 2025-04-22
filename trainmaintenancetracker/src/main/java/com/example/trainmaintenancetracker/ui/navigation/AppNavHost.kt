package com.example.trainmaintenancetracker.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.trainmaintenancetracker.ui.screen.taskdetail.taskDetailRoute
import com.example.trainmaintenancetracker.ui.screen.tasks.tasksRoute

@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: String,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        tasksRoute(navController)
        taskDetailRoute(navController)
    }
}
