package com.example.deviceinfosharing.domain.repository

import com.example.deviceinfosharing.domain.model.DeviceData
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for managing local device data
 * Provides abstraction for device data operations
 */
interface DeviceRepository {
    
    /**
     * Get local device data combining data from all providers
     * @return Flow of DeviceData updates
     */
    fun getLocalDeviceData(): Flow<DeviceData>
    
    /**
     * Upload device data to Firebase
     * @param deviceData The device data to upload
     */
    suspend fun uploadDeviceData(deviceData: DeviceData)
    
    /**
     * Save connected devices list
     * @param connectedDevices List of connected device IDs
     */
    suspend fun saveConnectedDevices(connectedDevices: List<String>)
    
    /**
     * Load connected devices list
     * @return List of connected device IDs
     */
    suspend fun loadConnectedDevices(): List<String>
}
