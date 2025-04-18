package com.example.messageapp.domain.sync

interface MessageSyncManager {
    fun monitorNetworkAndSync()
    fun schedulePeriodicSync(intervalHours: Long)
    fun triggerImmediateSync()
    fun cancelPeriodicSync()
}
