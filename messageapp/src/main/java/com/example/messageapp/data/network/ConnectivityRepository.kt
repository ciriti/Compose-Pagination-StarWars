package com.example.messageapp.data.network

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.messageapp.domain.network.ConnectivityObserver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.stateIn

interface ConnectivityRepository {
    val isConnected: StateFlow<Boolean>
}

class ConnectivityRepositoryImpl(
    connectivityObserver: ConnectivityObserver,
    coroutineScope: CoroutineScope,
    connectivityManager: ConnectivityManager

): ConnectivityRepository {

    private val initialValue = connectivityManager
        .activeNetwork?.let {
            connectivityManager.getNetworkCapabilities(it)
                ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        } ?: false

    override val isConnected: StateFlow<Boolean> = connectivityObserver
        .isConnected
        .stateIn(
            scope = coroutineScope,
            started = WhileSubscribed(5_000),
            initialValue = initialValue
        )
}
