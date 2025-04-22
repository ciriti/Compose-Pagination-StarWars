package com.example.googlenoteclone.data.repository

import androidx.work.NetworkType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface ConnectivityRepository {
    val isConnected: StateFlow<Boolean>
    val networkType: Flow<NetworkType>

    suspend fun checkConnection(): Boolean
    fun observeConnectionQuality(): Flow<ConnectionQuality>
}

/**
 * Represents the quality of network connection
 * Based on latency and bandwidth measurements
 */
enum class ConnectionQuality {
    /**
     * No network connection available
     */
    DISCONNECTED,

    /**
     * Very slow connection (e.g., 2G, extremely congested network)
     * Latency > 1000ms or bandwidth < 50kbps
     */
    POOR,

    /**
     * Moderate connection (e.g., 3G, slow WiFi)
     * Latency 300-1000ms or bandwidth 50-500kbps
     */
    MODERATE,

    /**
     * Good connection (e.g., 4G, decent WiFi)
     * Latency 100-300ms or bandwidth 500kbps-2Mbps
     */
    GOOD,

    /**
     * Excellent connection (e.g., 5G, fast WiFi)
     * Latency < 100ms and bandwidth > 2Mbps
     */
    EXCELLENT
}

/**
 * Data class containing detailed connection metrics
 */
data class NetworkMetrics(
    val latencyMs: Int,
    val bandwidthKbps: Int,
    val packetLossPercentage: Double,
    val jitterMs: Int
) {
    fun toQuality(): ConnectionQuality {
        return when {
            latencyMs > 1000 || bandwidthKbps < 50 -> ConnectionQuality.POOR
            latencyMs > 300 || bandwidthKbps < 500 -> ConnectionQuality.MODERATE
            latencyMs > 100 || bandwidthKbps < 2000 -> ConnectionQuality.GOOD
            else -> ConnectionQuality.EXCELLENT
        }
    }
}
