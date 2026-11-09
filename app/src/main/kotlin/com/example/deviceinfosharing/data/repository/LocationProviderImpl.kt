package com.example.deviceinfosharing.data.repository

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.os.Looper
import com.example.deviceinfosharing.domain.model.LocationData
import com.example.deviceinfosharing.domain.repository.LocationProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import timber.log.Timber
import java.util.Locale

/**
 * Implementation of LocationProvider using Google Play Services FusedLocationProvider
 * Handles device location retrieval and geocoding
 */
class LocationProviderImpl(
    private val context: Context,
    private val fusedLocationProviderClient: FusedLocationProviderClient
) : LocationProvider {

    private val geocoder = Geocoder(context, Locale.getDefault())
    private var locationCallback: LocationCallback? = null

    /**
     * Get current device location as a Flow
     * Performs reverse geocoding to get address information
     */
    @SuppressLint("MissingPermission")
    override fun getCurrentLocation(): Flow<LocationData> = callbackFlow {
        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            10000L // 10 seconds
        ).build()

        val callback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result.lastLocation?.let { location ->
                    try {
                        val addresses = geocoder.getFromLocation(
                            location.latitude,
                            location.longitude,
                            1
                        )

                        val locationData = if (addresses?.isNotEmpty() == true) {
                            val address = addresses[0]
                            LocationData(
                                latitude = location.latitude,
                                longitude = location.longitude,
                                city = address.locality ?: "Unknown",
                                country = address.countryName ?: "Unknown",
                                address = address.getAddressLine(0) ?: "Unknown"
                            )
                        } else {
                            LocationData(
                                latitude = location.latitude,
                                longitude = location.longitude,
                                city = "Unknown",
                                country = "Unknown",
                                address = "Unable to determine address"
                            )
                        }

                        trySend(locationData)
                    } catch (e: Exception) {
                        Timber.e(e, "Error geocoding location")
                        close(e)
                    }
                }
            }
        }

        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            callback,
            Looper.getMainLooper()
        )

        awaitClose {
            fusedLocationProviderClient.removeLocationUpdates(callback)
        }
    }

    /**
     * Start listening to location updates
     */
    @SuppressLint("MissingPermission")
    override fun startLocationUpdates() {
        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            5000L // 5 seconds
        ).build()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                Timber.d("Location update received: ${result.lastLocation}")
            }
        }

        locationCallback?.let {
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                it,
                Looper.getMainLooper()
            )
        }
    }

    /**
     * Stop listening to location updates
     */
    override fun stopLocationUpdates() {
        locationCallback?.let {
            fusedLocationProviderClient.removeLocationUpdates(it)
        }
    }
}
