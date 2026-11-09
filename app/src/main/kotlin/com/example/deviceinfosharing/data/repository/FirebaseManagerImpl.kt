package com.example.deviceinfosharing.data.repository

import com.example.deviceinfosharing.data.firebase.DeviceDataDTO
import com.example.deviceinfosharing.domain.model.DeviceData
import com.example.deviceinfosharing.domain.repository.FirebaseManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * Implementation of FirebaseManager for Firebase Realtime Database operations
 * Manages uploading and listening to device data
 */
@Singleton
class FirebaseManagerImpl @Inject constructor(
    private val database: FirebaseDatabase
) : FirebaseManager {
    
    private val deviceListeners = mutableMapOf<String, ValueEventListener>()
    
    override suspend fun uploadDeviceData(deviceData: DeviceData) {
        return suspendCancellableCoroutine { continuation ->
            try {
                val deviceId = deviceData.deviceId
                val ref = database.getReference("devices/$deviceId")
                val dto = DeviceDataDTO.fromDomainModel(deviceData)
                
                ref.setValue(dto)
                    .addOnSuccessListener {
                        Timber.d("Device data uploaded successfully for $deviceId")
                        continuation.resume(Unit)
                    }
                    .addOnFailureListener { e ->
                        Timber.e(e, "Failed to upload device data for $deviceId")
                        continuation.resumeWithException(e)
                    }
            } catch (e: Exception) {
                Timber.e(e, "Error uploading device data")
                continuation.resumeWithException(e)
            }
        }
    }
    
    override fun listenToRemoteDevice(remoteDeviceId: String): Flow<DeviceData> = callbackFlow {
        val ref = database.getReference("devices/$remoteDeviceId")
        
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    val dto = snapshot.getValue(DeviceDataDTO::class.java)
                    if (dto != null) {
                        val deviceData = dto.toDomainModel()
                        trySend(deviceData)
                        Timber.d("Received device data for $remoteDeviceId")
                    } else {
                        Timber.w("No data found for device $remoteDeviceId")
                    }
                } catch (e: Exception) {
                    Timber.e(e, "Error parsing device data for $remoteDeviceId")
                    close(e)
                }
            }
            
            override fun onCancelled(error: DatabaseError) {
                Timber.e(error.toException(), "Firebase listener cancelled for $remoteDeviceId")
                close(error.toException())
            }
        }
        
        ref.addValueEventListener(listener)
        deviceListeners[remoteDeviceId] = listener
        
        Timber.d("Started listening to device $remoteDeviceId")
        
        awaitClose {
            stopListeningToRemoteDevice(remoteDeviceId)
        }
    }
    
    override fun stopListeningToRemoteDevice(remoteDeviceId: String) {
        try {
            val listener = deviceListeners[remoteDeviceId]
            if (listener != null) {
                val ref = database.getReference("devices/$remoteDeviceId")
                ref.removeEventListener(listener)
                deviceListeners.remove(remoteDeviceId)
                Timber.d("Stopped listening to device $remoteDeviceId")
            }
        } catch (e: Exception) {
            Timber.e(e, "Error stopping listener for $remoteDeviceId")
        }
    }
    
    override fun stopAllListeners() {
        try {
            deviceListeners.forEach { (deviceId, listener) ->
                val ref = database.getReference("devices/$deviceId")
                ref.removeEventListener(listener)
            }
            deviceListeners.clear()
            Timber.d("Stopped all device listeners")
        } catch (e: Exception) {
            Timber.e(e, "Error stopping all listeners")
        }
    }
}
