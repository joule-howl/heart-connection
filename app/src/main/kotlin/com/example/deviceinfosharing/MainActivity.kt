package com.example.deviceinfosharing

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.content.Context
import android.os.Build
import androidx.activity.compose.setContent
import com.example.deviceinfosharing.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.UUID

/**
 * MainActivity: Main entry point for the Device Info Sharing application
 * 
 * Responsibilities:
 * - Display local device information
 * - Display connected remote devices
 * - Handle user interactions
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Timber.d("MainActivity: Created")
        
        // Get device info
        val deviceId = getDeviceId()
        val deviceName = Build.DEVICE
        val androidVersion = Build.VERSION.RELEASE
        val manufacturer = Build.MANUFACTURER
        
        // Set content using Compose
        setContent {
            MaterialTheme {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black)
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Device Info Sharing",
                        fontSize = 24.sp,
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    
                    Text(
                        text = "Device ID: $deviceId",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    
                    Text(
                        text = "Device: $deviceName",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    
                    Text(
                        text = "Manufacturer: $manufacturer",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    
                    Text(
                        text = "Android: $androidVersion",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    
                    Text(
                        text = "Build Model: ${Build.MODEL}",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
        
        initializeApp()
    }
    
    /**
     * Get or generate device ID
     */
    private fun getDeviceId(): String {
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
     * Initialize application - called when MainActivity is created
     * Sets up device ID, Firebase connection, and starts monitoring services
     */
    private fun initializeApp() {
        Timber.d("MainActivity: Initializing app...")
        
        // TODO: Implement device ID generation and persistence
        // TODO: Implement location updates start
        // TODO: Implement weather fetching
        // TODO: Implement battery monitoring
        // TODO: Implement connectivity monitoring
    }
}
