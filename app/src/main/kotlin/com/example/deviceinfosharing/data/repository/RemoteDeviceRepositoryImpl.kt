package com.example.deviceinfosharing.data.repository

import com.example.deviceinfosharing.data.local.SharedPreferencesManager
import com.example.deviceinfosharing.domain.model.DeviceData
import com.example.deviceinfosharing.domain.repository.FirebaseManager
import com.example.deviceinfosharing.domain.repository.RemoteDeviceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

/**
 * Implementation of RemoteDeviceRepository
 * 
 * Handles listening to remote device data from Firebase with support for custom device names
 * stored in SharedPreferences. Enhances Firebase device data with locally stored names.
 */
class RemoteDeviceRepositoryImpl @Inject constructor(
    private val firebaseManager: FirebaseManager,
    private val sharedPreferencesManager: SharedPreferencesManager
) : RemoteDeviceRepository {

    /**
     * Observe a remote device's data updates in real-time
     * 
     * Subscribes to Firebase updates for the specified device and enriches the data
     * with custom device names loaded from SharedPreferences. If a custom name exists
     * for the device, it will be used instead of the Firebase name.
     * 
     * @param remoteDeviceId The unique identifier of the remote device to observe
     * @return Flow<DeviceData> that emits updated device data whenever it changes in Firebase.
     *         - Enriched with custom device name from SharedPreferences if available
     *         - Errors are logged and re-emitted in the Flow
     *         - The Flow completes when stopObservingRemoteDevice is called
     */
    override fun observeRemoteDevice(remoteDeviceId: String): Flow<DeviceData> {
        Timber.d("Starting to observe remote device: $remoteDeviceId")
        
        return firebaseManager.listenToRemoteDevice(remoteDeviceId)
            .map { deviceData ->
                // Load custom device name from SharedPreferences
                val customDeviceName = sharedPreferencesManager.loadDeviceName(remoteDeviceId)
                
                // If custom name exists, use it; otherwise use the Firebase device name
                if (customDeviceName.isNotEmpty()) {
                    Timber.d("Using custom device name for $remoteDeviceId: $customDeviceName")
                    deviceData.copy(deviceName = customDeviceName)
                } else {
                    // Use the device name from Firebase (if available)
                    Timber.d("Using Firebase device name for $remoteDeviceId: ${deviceData.deviceName}")
                    deviceData
                }
            }
            .catch { error ->
                Timber.e(error, "Error observing remote device $remoteDeviceId")
                // Re-emit the error so the caller can handle it
                throw error
            }
    }

    /**
     * Stop observing updates from a specific remote device
     * 
     * Unsubscribes from Firebase listener for the specified device.
     * This is important for resource cleanup to prevent memory leaks
     * and unnecessary network traffic.
     * 
     * @param remoteDeviceId The unique identifier of the remote device to stop observing
     */
    override fun stopObservingRemoteDevice(remoteDeviceId: String) {
        Timber.d("Stopping observation of remote device: $remoteDeviceId")
        firebaseManager.stopListeningToRemoteDevice(remoteDeviceId)
    }
}
