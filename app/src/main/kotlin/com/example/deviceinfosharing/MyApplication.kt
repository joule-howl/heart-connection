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
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            Timber.d("MyApplication: Created (DEBUG mode)")
        } else {
            // In production, plant a custom tree that sends logs to remote service
            Timber.plant(Timber.DebugTree())
            Timber.d("MyApplication: Created (RELEASE mode)")
        }
        
        Timber.d("MyApplication: Hilt dependency injection enabled")
    }
}
