package com.example.deviceinfosharing

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.deviceinfosharing.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

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

        // Initialize view binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        Timber.d("MainActivity: Created")
        
        // Initialize app (can be extended to set up initial state)
        initializeApp()
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
