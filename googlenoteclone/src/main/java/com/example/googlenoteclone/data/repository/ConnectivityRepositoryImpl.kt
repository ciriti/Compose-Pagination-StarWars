package com.example.googlenoteclone.data.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.work.NetworkType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.stateIn
import kotlin.coroutines.CoroutineContext

class ConnectivityRepositoryImpl(
    private val context: Context,
    private val connectivityManager: ConnectivityManager,
    private val coroutineContext: CoroutineContext
) : ConnectivityRepository {

    private val networkRequest = NetworkRequest.Builder()
        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        .build()

    override val isConnected: StateFlow<Boolean> = callbackFlow {
        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                trySend(true)
            }

            override fun onLost(network: Network) {
                trySend(false)
            }
        }

        connectivityManager.registerNetworkCallback(networkRequest, callback)
        awaitClose { connectivityManager.unregisterNetworkCallback(callback) }
    }.stateIn(
        scope = CoroutineScope(coroutineContext),
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )

    val conn: StateFlow<Boolean> = callbackFlow {
        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                trySend(true)
            }

            override fun onLost(network: Network) {
                trySend(false)
            }
        }
        connectivityManager.registerNetworkCallback(networkRequest, callback)
        awaitClose { connectivityManager.unregisterNetworkCallback(callback) }
    }.stateIn(
        scope = CoroutineScope(coroutineContext),
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = false
    )

    override val networkType: Flow<NetworkType> = callbackFlow {
        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onCapabilitiesChanged(
                network: Network,
                networkCapabilities: NetworkCapabilities
            ) {
                val type = when {
                    networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> NetworkType.UNMETERED
                    networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> NetworkType.METERED
                    else -> NetworkType.CONNECTED
                }
                trySend(type)
            }
        }

        connectivityManager.registerNetworkCallback(networkRequest, callback)
        awaitClose { connectivityManager.unregisterNetworkCallback(callback) }
    }

    override suspend fun checkConnection(): Boolean {
        TODO("Not yet implemented")
    }

    override fun observeConnectionQuality(): Flow<ConnectionQuality> {
        TODO("Not yet implemented")
    }
}
