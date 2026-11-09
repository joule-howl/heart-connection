package com.example.deviceinfosharing.domain.model

/**
 * Domain Model representing weather condition information.
 *
 * This data class encapsulates meteorological data collected at a specific location
 * and time. It provides weather metrics that are independent of any framework
 * and can be used throughout the application domain.
 *
 * @property temperature Current temperature in Celsius
 * @property weatherCode Standardized weather code (e.g., from WMO codes)
 * @property description Human-readable description of weather conditions
 * @property humidity Relative humidity as a percentage (0-100)
 * @property windSpeed Wind speed in km/h or m/s as per standard
 *
 * @author Device Info Sharing Team
 * @since 1.0
 */
data class WeatherData(
    val temperature: Double,
    val weatherCode: Int,
    val description: String,
    val humidity: Int,
    val windSpeed: Double
)
