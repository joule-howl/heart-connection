package com.example.deviceinfosharing.domain.repository

import com.example.deviceinfosharing.domain.model.DeviceData
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for remote device data operations
 * Handles listening to data from other devices via Firebase
 */
interface RemoteDeviceRepository {
    /**
     * Observe remote device data updates in real-time
     * 
     * @param remoteDeviceId The ID of the remote device to observe
     * @return Flow of DeviceData with updates from Firebase
     *         Device name is loaded from local SharedPreferences
     */
    fun observeRemoteDevice(remoteDeviceId: String): Flow<DeviceData>

    /**
     * Stop observing a specific remote device
     * 
     * @param remoteDeviceId The ID of the remote device to stop observing
     */
    fun stopObservingRemoteDevice(remoteDeviceId: String)
}
