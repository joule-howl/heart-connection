package com.example.deviceinfosharing.data.repository

import com.example.deviceinfosharing.data.api.WeatherApiService
import com.example.deviceinfosharing.domain.model.WeatherData
import com.example.deviceinfosharing.domain.repository.WeatherService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import timber.log.Timber

/**
 * Implementation of WeatherService using a weather API
 * Handles weather data retrieval and transformation
 */
class WeatherServiceImpl(
    private val weatherApiService: WeatherApiService
) : WeatherService {

    /**
     * Get weather data for a specific location using latitude and longitude
     */
    override fun getWeatherByLocation(latitude: Double, longitude: Double): Flow<WeatherData> =
        flow {
            try {
                val response = weatherApiService.getWeatherByCoordinates(latitude, longitude)
                emit(response)
            } catch (e: Exception) {
                Timber.e(e, "Error fetching weather by location")
                throw e
            }
        }.catch { exception ->
            Timber.e(exception, "Weather service error")
            throw exception
        }

    /**
     * Get weather data by city name
     */
    override fun getWeatherByCity(cityName: String): Flow<WeatherData> =
        flow {
            try {
                val response = weatherApiService.getWeatherByCity(cityName)
                emit(response)
            } catch (e: Exception) {
                Timber.e(e, "Error fetching weather by city")
                throw e
            }
        }.catch { exception ->
            Timber.e(exception, "Weather service error")
            throw exception
        }
}
