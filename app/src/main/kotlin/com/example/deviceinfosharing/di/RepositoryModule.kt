package com.example.deviceinfosharing.di

import com.example.deviceinfosharing.data.local.SharedPreferencesManager
import com.example.deviceinfosharing.data.repository.DeviceRepositoryImpl
import com.example.deviceinfosharing.domain.repository.BatteryMonitor
import com.example.deviceinfosharing.domain.repository.ConnectivityManagerWrapper
import com.example.deviceinfosharing.domain.repository.DeviceRepository
import com.example.deviceinfosharing.domain.repository.FirebaseManager
import com.example.deviceinfosharing.domain.repository.LocationProvider
import com.example.deviceinfosharing.domain.repository.WeatherService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt Module for Repository Dependencies
 * Provides singleton instances of repository implementations
 */
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    /**
     * Provides a singleton instance of DeviceRepository
     * for managing local device data
     */
    @Singleton
    @Provides
    fun provideDeviceRepository(
        firebaseManager: FirebaseManager,
        locationProvider: LocationProvider,
        weatherService: WeatherService,
        batteryMonitor: BatteryMonitor,
        connectivityManager: ConnectivityManagerWrapper,
        sharedPreferencesManager: SharedPreferencesManager
    ): DeviceRepository {
        return DeviceRepositoryImpl(
            firebaseManager = firebaseManager,
            locationProvider = locationProvider,
            weatherService = weatherService,
            batteryMonitor = batteryMonitor,
            connectivityManager = connectivityManager,
            sharedPreferencesManager = sharedPreferencesManager
        )
    }
}
