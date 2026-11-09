package com.example.deviceinfosharing.data.firebase

import com.google.gson.annotations.SerializedName
import com.example.deviceinfosharing.domain.model.DeviceData

/**
 * Data Transfer Object (DTO) for Firebase Realtime Database
 * This class handles serialization/deserialization with Firebase
 * Separate from Domain Model to maintain Separation of Concerns
 */
data class DeviceDataDTO(
    @SerializedName("deviceId")
    val deviceId: String = "",
    
    @SerializedName("deviceName")
    val deviceName: String = "",
    
    @SerializedName("timestamp")
    val timestamp: Long = 0L,
    
    @SerializedName("temperature")
    val temperature: Double = 0.0,
    
    @SerializedName("location")
    val location: String = "",
    
    @SerializedName("weatherDescription")
    val weatherDescription: String = "",
    
    @SerializedName("batteryPercentage")
    val batteryPercentage: Int = 0,
    
    @SerializedName("connectionStatus")
    val connectionStatus: String = "Offline"
) {
    /**
     * Convert DTO to Domain Model
     */
    fun toDomainModel(): DeviceData {
        return DeviceData(
            deviceId = deviceId,
            deviceName = deviceName,
            timestamp = timestamp,
            temperature = temperature,
            location = location,
            weatherDescription = weatherDescription,
            batteryPercentage = batteryPercentage,
            connectionStatus = connectionStatus
        )
    }

    companion object {
        /**
         * Convert Domain Model to DTO for Firebase upload
         */
        fun fromDomainModel(device: DeviceData): DeviceDataDTO {
            return DeviceDataDTO(
                deviceId = device.deviceId,
                deviceName = device.deviceName,
                timestamp = device.timestamp,
                temperature = device.temperature,
                location = device.location,
                weatherDescription = device.weatherDescription,
                batteryPercentage = device.batteryPercentage,
                connectionStatus = device.connectionStatus
            )
        }
    }
}
