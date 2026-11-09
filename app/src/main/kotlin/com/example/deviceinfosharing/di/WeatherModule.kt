package com.example.deviceinfosharing.di

import com.example.deviceinfosharing.data.api.WeatherApiService
import com.example.deviceinfosharing.data.repository.WeatherServiceImpl
import com.example.deviceinfosharing.domain.repository.WeatherService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Hilt Module for Weather Dependencies
 * Provides singleton instances of weather-related services
 */
@Module
@InstallIn(SingletonComponent::class)
object WeatherModule {

    /**
     * Provides a singleton instance of WeatherApiService
     * for API communication with weather endpoints
     */
    @Singleton
    @Provides
    fun provideWeatherApiService(retrofit: Retrofit): WeatherApiService {
        return retrofit.create(WeatherApiService::class.java)
    }

    /**
     * Provides a singleton instance of WeatherService
     * for weather data operations
     */
    @Singleton
    @Provides
    fun provideWeatherService(weatherApiService: WeatherApiService): WeatherService {
        return WeatherServiceImpl(weatherApiService)
    }
}
