package com.example.deviceinfosharing

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.content.Context
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.UUID
import com.example.deviceinfosharing.presentation.ui.DeviceInfoScreen

/**
 * MainActivity: Main entry point for the Device Info Sharing application
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Timber.d("MainActivity: Created")
        
        val deviceId = generateOrGetDeviceId()
        
        setContent {
            DeviceInfoScreen(deviceId = deviceId)
        }
        
        initializeApp()
    }
    
    /**
     * Generate or retrieve stored device ID
     */
    private fun generateOrGetDeviceId(): String {
        val prefs = getSharedPreferences("device_prefs", Context.MODE_PRIVATE)
        var deviceId = prefs.getString("device_id", null)
        
        if (deviceId == null) {
            deviceId = UUID.randomUUID().toString()
            prefs.edit().putString("device_id", deviceId).apply()
            Timber.d("MainActivity: Generated new Device ID: $deviceId")
        }
        
        return deviceId
    }
    
    /**
     * Initialize application
     */
    private fun initializeApp() {
        Timber.d("MainActivity: Initializing app...")
    }
}
