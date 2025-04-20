package com.example.messageapp.data.connectivity

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.example.messageapp.domain.network.ConnectivityObserver
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

internal class ConnectivityObserverImpl(
    private val context: Context
) : ConnectivityObserver {

    override val isConnected: Flow<Boolean> = callbackFlow {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        val callback = object: ConnectivityManager.NetworkCallback(){
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
}
