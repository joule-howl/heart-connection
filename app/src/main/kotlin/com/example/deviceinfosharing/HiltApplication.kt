package com.example.deviceinfosharing

import android.app.Application
import com.example.deviceinfosharing.data.firebase.FirebaseConfig
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

/**
 * Hilt Application class that enables dependency injection
 * across the entire application
 */
@HiltAndroidApp
class HiltApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        
        // Initialize Timber logging in debug builds
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            Timber.d("HiltApplication: Created (DEBUG mode)")
        } else {
            Timber.plant(Timber.DebugTree())
            Timber.d("HiltApplication: Created (RELEASE mode)")
        }
        
        // Initialize Firebase
        initializeFirebase()
    }
    
    /**
     * Initialize Firebase authentication and database connection
     */
    private fun initializeFirebase() {
        Timber.d("HiltApplication: Initializing Firebase...")
        
        FirebaseConfig.initializeFirebase(
            onSuccess = {
                Timber.d("HiltApplication: Firebase initialization successful")
                Timber.d("HiltApplication: Firebase Auth UID: ${FirebaseConfig.getCurrentUserId()}")
                FirebaseConfig.logFirebaseInfo()
            },
            onFailure = { exception ->
                Timber.e(exception, "HiltApplication: Firebase initialization failed")
            }
        )
    }
}
