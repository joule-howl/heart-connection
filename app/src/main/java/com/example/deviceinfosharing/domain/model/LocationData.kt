package com.example.deviceinfosharing.domain.model

/**
 * Domain Model representing geographical location information.
 *
 * This data class encapsulates location coordinates and geographical identifiers
 * without any framework dependencies. It provides precise location data for
 * device tracking and location-based services.
 *
 * @property latitude Latitude coordinate in decimal degrees (range: -90 to 90)
 * @property longitude Longitude coordinate in decimal degrees (range: -180 to 180)
 * @property city Name of the city where the device is located
 * @property country Name of the country where the device is located
 * @property address Full address string of the device location
 *
 * @author Device Info Sharing Team
 * @since 1.0
 */
data class LocationData(
    val latitude: Double,
    val longitude: Double,
    val city: String,
    val country: String,
    val address: String
)
