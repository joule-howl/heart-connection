package com.example.deviceinfosharing.di

import android.content.Context
import com.example.deviceinfosharing.data.repository.BatteryMonitorImpl
import com.example.deviceinfosharing.data.repository.ConnectivityManagerWrapperImpl
import com.example.deviceinfosharing.domain.repository.BatteryMonitor
import com.example.deviceinfosharing.domain.repository.ConnectivityManagerWrapper
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt Module for Device Monitoring Dependencies
 * Provides singleton instances of battery and connectivity monitors
 */
@Module
@InstallIn(SingletonComponent::class)
object MonitoringModule {

    /**
     * Provides a singleton instance of BatteryMonitor
     * for battery status monitoring
     */
    @Singleton
    @Provides
    fun provideBatteryMonitor(
        context: Context
    ): BatteryMonitor {
        return BatteryMonitorImpl(context)
    }

    /**
     * Provides a singleton instance of ConnectivityManagerWrapper
     * for network connectivity monitoring
     */
    @Singleton
    @Provides
    fun provideConnectivityManager(
        context: Context
    ): ConnectivityManagerWrapper {
        return ConnectivityManagerWrapperImpl(context)
    }
}
