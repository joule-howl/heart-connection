package com.example.deviceinfosharing.data.api

import com.example.deviceinfosharing.domain.model.WeatherData
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit API service interface for weather API endpoints
 * Handles HTTP requests to weather services
 */
interface WeatherApiService {

    /**
     * Get weather information by geographic coordinates
     * @param latitude Location latitude
     * @param longitude Location longitude
     * @return Weather data for the specified location
     */
    @GET("weather")
    suspend fun getWeatherByCoordinates(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double
    ): WeatherData

    /**
     * Get weather information by city name
     * @param city City name
     * @return Weather data for the specified city
     */
    @GET("weather")
    suspend fun getWeatherByCity(
        @Query("q") city: String
    ): WeatherData
}
