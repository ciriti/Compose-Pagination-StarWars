package com.example.trainmaintenancetracker.domain.data.repository

import kotlinx.coroutines.flow.StateFlow

interface ConnectivityRepository {
    val isConnected: StateFlow<Boolean>
}
