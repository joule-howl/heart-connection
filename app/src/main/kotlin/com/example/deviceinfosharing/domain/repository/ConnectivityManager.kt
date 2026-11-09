package com.example.deviceinfosharing.domain.repository

import kotlinx.coroutines.flow.Flow

/**
 * Interface for network connectivity monitoring
 */
interface ConnectivityManagerWrapper {
    
    /**
     * Get connectivity status updates as a Flow
     * @return Flow of Boolean - true if connected, false otherwise
     */
    fun getConnectivityStatus(): Flow<Boolean>
    
    /**
     * Start monitoring connectivity changes
     */
    fun startMonitoring()
    
    /**
     * Stop monitoring connectivity changes
     */
    fun stopMonitoring()
    
    /**
     * Check if device is currently connected
     * @return true if connected, false otherwise
     */
    fun isConnected(): Boolean
}
