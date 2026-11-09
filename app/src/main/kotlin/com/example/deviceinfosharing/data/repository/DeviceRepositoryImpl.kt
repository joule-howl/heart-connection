package com.example.deviceinfosharing.data.repository

import com.example.deviceinfosharing.data.local.SharedPreferencesManager
import com.example.deviceinfosharing.domain.model.DeviceData
import com.example.deviceinfosharing.domain.repository.BatteryMonitor
import com.example.deviceinfosharing.domain.repository.ConnectivityManagerWrapper
import com.example.deviceinfosharing.domain.repository.DeviceRepository
import com.example.deviceinfosharing.domain.repository.FirebaseManager
import com.example.deviceinfosharing.domain.repository.LocationProvider
import com.example.deviceinfosharing.domain.repository.WeatherService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of DeviceRepository for managing local device data
 * 
 * Combines data from multiple providers (location, weather, battery, connectivity)
 * Handles upload to Firebase with retry logic and error handling
 * Uses Kotlin Coroutines and Flow for reactive data management
 */
@Singleton
class DeviceRepositoryImpl @Inject constructor(
    private val firebaseManager: FirebaseManager,
    private val locationProvider: LocationProvider,
    private val weatherService: WeatherService,
    private val batteryMonitor: BatteryMonitor,
    private val connectivityManager: ConnectivityManagerWrapper,
    private val sharedPreferencesManager: SharedPreferencesManager
) : DeviceRepository {
    
    companion object {
        private const val MAX_RETRY_ATTEMPTS = 3
        private const val INITIAL_RETRY_DELAY_MS = 5000L // 5 seconds
        private const val RETRY_BACKOFF_MULTIPLIER = 1.5
    }
    
    override fun getLocalDeviceData(): Flow<DeviceData> = flow {
        try {
            // Get device ID
            val deviceId = sharedPreferencesManager.loadDeviceId()
            if (deviceId.isEmpty()) {
                Timber.e("Device ID not found")
                throw IllegalStateException("Device ID not initialized")
            }
            
            // Get device name
            val deviceName = sharedPreferencesManager.loadDeviceName(deviceId)
                .takeIf { it.isNotEmpty() } ?: "My Device"
            
            // Combine location and battery/connectivity flows
            val locationAndStatusFlow = combine(
                locationProvider.getCurrentLocation(),
                batteryMonitor.getBatteryStatus(),
                connectivityManager.getConnectivityStatus()
            ) { location, battery, isConnected ->
                Triple(location, battery, isConnected)
            }
            
            // For each location update, fetch weather data
            locationAndStatusFlow.collect { (location, battery, isConnected) ->
                try {
                    // Fetch weather for the current location
                    weatherService.getWeatherByLocation(location.latitude, location.longitude)
                        .catch { exception ->
                            Timber.e(exception, "Weather service error for location ${location.city}")
                            // Emit default weather data on error
                            emit(com.example.deviceinfosharing.domain.model.WeatherData(
                                temperature = 0.0,
                                weatherCode = -1,
                                description = "Unknown",
                                humidity = 0,
                                windSpeed = 0.0
                            ))
                        }
                        .collect { weather ->
                            val timestamp = System.currentTimeMillis()
                            val connectionStatus = if (isConnected) "Online" else "Offline"
                            
                            val deviceData = DeviceData(
                                deviceId = deviceId,
                                deviceName = deviceName,
                                timestamp = timestamp,
                                temperature = weather.temperature,
                                location = "${location.city}, ${location.country}",
                                weatherDescription = weather.description,
                                batteryPercentage = battery.percentage,
                                connectionStatus = connectionStatus
                            )
                            
                            // Upload device data to Firebase on each update
                            uploadDeviceDataWithRetry(deviceData)
                            
                            emit(deviceData)
                        }
                } catch (e: Exception) {
                    Timber.e(e, "Error processing device data")
                    // Continue processing on error
                }
            }
                
        } catch (e: Exception) {
            Timber.e(e, "Error in getLocalDeviceData")
            throw e
        }
    }
    
    override suspend fun uploadDeviceData(deviceData: DeviceData) {
        withContext(Dispatchers.IO) {
            try {
                firebaseManager.uploadDeviceData(deviceData)
                Timber.d("Device data uploaded successfully")
            } catch (e: Exception) {
                Timber.e(e, "Failed to upload device data")
                throw e
            }
        }
    }
    
    /**
     * Upload device data with retry logic and exponential backoff
     * Retries up to MAX_RETRY_ATTEMPTS times with increasing delays
     */
    private suspend fun uploadDeviceDataWithRetry(deviceData: DeviceData) {
        var lastException: Exception? = null
        var retryDelay = INITIAL_RETRY_DELAY_MS
        
        for (attempt in 1..MAX_RETRY_ATTEMPTS) {
            try {
                uploadDeviceData(deviceData)
                return // Success, exit early
            } catch (e: Exception) {
                lastException = e
                Timber.w(e, "Upload attempt $attempt/$MAX_RETRY_ATTEMPTS failed")
                
                if (attempt < MAX_RETRY_ATTEMPTS) {
                    // Wait before retrying with exponential backoff
                    delay(retryDelay.toLong())
                    retryDelay = (retryDelay * RETRY_BACKOFF_MULTIPLIER).toLong()
                }
            }
        }
        
        // All retries exhausted
        Timber.e(lastException, "Failed to upload device data after $MAX_RETRY_ATTEMPTS attempts")
    }
    
    override suspend fun saveConnectedDevices(connectedDevices: List<String>) {
        withContext(Dispatchers.IO) {
            try {
                sharedPreferencesManager.saveConnectedDevices(connectedDevices)
                Timber.d("Connected devices saved: ${connectedDevices.size} devices")
            } catch (e: Exception) {
                Timber.e(e, "Error saving connected devices")
                throw e
            }
        }
    }
    
    override suspend fun loadConnectedDevices(): List<String> {
        return withContext(Dispatchers.IO) {
            try {
                val devices = sharedPreferencesManager.loadConnectedDevices()
                Timber.d("Connected devices loaded: ${devices.size} devices")
                devices
            } catch (e: Exception) {
                Timber.e(e, "Error loading connected devices")
                emptyList()
            }
        }
    }
}
