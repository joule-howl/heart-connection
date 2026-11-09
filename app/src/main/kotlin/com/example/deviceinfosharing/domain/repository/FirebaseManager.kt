package com.example.deviceinfosharing.domain.repository

import com.example.deviceinfosharing.domain.model.DeviceData
import kotlinx.coroutines.flow.Flow

/**
 * Interface for Firebase Realtime Database operations
 */
interface FirebaseManager {
    
    /**
     * Upload device data to Firebase
     * @param deviceData The device data to upload
     */
    suspend fun uploadDeviceData(deviceData: DeviceData)
    
    /**
     * Listen to remote device data updates
     * @param remoteDeviceId The device ID to listen to
     * @return Flow of DeviceData updates
     */
    fun listenToRemoteDevice(remoteDeviceId: String): Flow<DeviceData>
    
    /**
     * Stop listening to a remote device
     * @param remoteDeviceId The device ID to stop listening to
     */
    fun stopListeningToRemoteDevice(remoteDeviceId: String)
    
    /**
     * Stop listening to all remote devices
     */
    fun stopAllListeners()
}
