package com.example.trainmaintenancetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.trainmaintenancetracker.ui.navigation.AppNavHost
import com.example.trainmaintenancetracker.ui.navigation.Route
import com.example.trainmaintenancetracker.ui.theme.TrainMaintenanceTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TrainMaintenanceTheme {
                val navController = rememberNavController()
                AppNavHost(
                    navController = navController,
                    startDestination = Route.Tasks.route
                )
            }
        }
    }
}
