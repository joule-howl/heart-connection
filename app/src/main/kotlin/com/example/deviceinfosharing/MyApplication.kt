package com.example.deviceinfosharing

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

/**
 * MyApplication: Custom Application class for Device Info Sharing
 * 
 * Responsibilities:
 * - Enable Hilt dependency injection
 * - Initialize logging with Timber
 * - Set up Firebase initialization
 */
@HiltAndroidApp
class MyApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        
        // Initialize Timber logging
        Timber.plant(Timber.DebugTree())
        Timber.d("MyApplication: Created")
        
        Timber.d("MyApplication: Hilt dependency injection enabled")
    }
}
