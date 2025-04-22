package com.example.trainmaintenancetracker.data.repository

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.example.trainmaintenancetracker.domain.data.repository.ConnectivityRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.stateIn


class ConnectivityRepositoryImpl(
    coroutineScope: CoroutineScope,
    connectivityManager: ConnectivityManager
) : ConnectivityRepository {

    private val initialValue = connectivityManager
        .activeNetwork?.let {
            connectivityManager.getNetworkCapabilities(it)
                ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        } ?: false

    override val isConnected: StateFlow<Boolean> = callbackFlow {
        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                channel.trySend(true)
                println("====================== onAvailable ===================")
            }

            override fun onLost(network: Network) {
                channel.trySend(false)
                println("====================== NonAvailable ===================")
            }
        }

        connectivityManager.registerNetworkCallback(request, callback)
        println("=================== registerNetworkCallback ===================")

        awaitClose {
            connectivityManager.unregisterNetworkCallback(callback)
            println("=================== unregisterNetworkCallback ===================")
        }
    }
        .stateIn(
            scope = coroutineScope,
            started = WhileSubscribed(5_000),
            initialValue = initialValue
        )
}
