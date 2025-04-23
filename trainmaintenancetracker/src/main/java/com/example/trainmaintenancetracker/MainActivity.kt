package com.example.trainmaintenancetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.compose.rememberNavController
import com.example.trainmaintenancetracker.data.sync.TaskSyncManager
import com.example.trainmaintenancetracker.domain.data.repository.ConnectivityRepository
import com.example.trainmaintenancetracker.ui.navigation.AppNavHost
import com.example.trainmaintenancetracker.ui.navigation.Route
import com.example.trainmaintenancetracker.ui.theme.TrainMaintenanceTheme
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val connectivityRepository: ConnectivityRepository by inject()
    private val syncManager: TaskSyncManager by inject()

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

        // reconnect
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                connectivityRepository
                    .isConnected
                    .filter { it }
                    .collect { _ ->
                        syncManager.triggerImmediateSync()
                    }
            }
        }
    }
}
