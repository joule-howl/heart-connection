package com.example.deviceinfosharing.domain.repository

import com.example.deviceinfosharing.domain.model.LocationData
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for location data operations
 */
interface LocationProvider {
    /**
     * Get current device location
     * @return Flow of LocationData
     */
    fun getCurrentLocation(): Flow<LocationData>

    /**
     * Start listening to location updates
     */
    fun startLocationUpdates()

    /**
     * Stop listening to location updates
     */
    fun stopLocationUpdates()
}
