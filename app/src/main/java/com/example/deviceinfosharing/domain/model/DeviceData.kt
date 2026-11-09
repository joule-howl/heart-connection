package com.example.deviceinfosharing.domain.model

/**
 * Domain Model representing comprehensive device information.
 *
 * This data class encapsulates all essential device metrics and status information
 * collected from a device. It is framework-independent and represents the core
 * business logic of the device data sharing domain.
 *
 * @property deviceId Unique identifier for the device
 * @property deviceName Human-readable name of the device
 * @property timestamp Unix timestamp (milliseconds) when the data was collected
 * @property temperature Current device temperature in Celsius
 * @property location Current location description or address of the device
 * @property weatherDescription Description of current weather conditions
 * @property batteryPercentage Battery level as a percentage (0-100)
 * @property connectionStatus Connection status of the device ("Online" or "Offline")
 *
 * @author Device Info Sharing Team
 * @since 1.0
 */
data class DeviceData(
    val deviceId: String,
    val deviceName: String,
    val timestamp: Long,
    val temperature: Double,
    val location: String,
    val weatherDescription: String,
    val batteryPercentage: Int,
    val connectionStatus: String // "Online" | "Offline"
)
