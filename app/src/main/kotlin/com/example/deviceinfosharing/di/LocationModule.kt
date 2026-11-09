package com.example.deviceinfosharing.di

import android.content.Context
import com.example.deviceinfosharing.data.repository.LocationProviderImpl
import com.example.deviceinfosharing.domain.repository.LocationProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt Module for Location Dependencies
 * Provides singleton instances of location-related services
 */
@Module
@InstallIn(SingletonComponent::class)
object LocationModule {

    /**
     * Provides a singleton instance of FusedLocationProviderClient
     * for accessing device location
     */
    @Singleton
    @Provides
    fun provideFusedLocationProviderClient(
        @ApplicationContext context: Context
    ): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(context)
    }

    /**
     * Provides a singleton instance of LocationProvider
     * for managing location data retrieval
     */
    @Singleton
    @Provides
    fun provideLocationProvider(
        @ApplicationContext context: Context,
        fusedLocationProviderClient: FusedLocationProviderClient
    ): LocationProvider {
        return LocationProviderImpl(context, fusedLocationProviderClient)
    }
}
