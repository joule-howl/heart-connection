package com.example.deviceinfosharing.domain.repository

import com.example.deviceinfosharing.domain.model.WeatherData
import kotlinx.coroutines.flow.Flow

/**
 * Service interface for weather data operations
 */
interface WeatherService {
    /**
     * Get weather data for a specific location
     * @param latitude Location latitude
     * @param longitude Location longitude
     * @return Flow of WeatherData
     */
    fun getWeatherByLocation(latitude: Double, longitude: Double): Flow<WeatherData>

    /**
     * Get weather data by city name
     * @param cityName Name of the city
     * @return Flow of WeatherData
     */
    fun getWeatherByCity(cityName: String): Flow<WeatherData>
}
