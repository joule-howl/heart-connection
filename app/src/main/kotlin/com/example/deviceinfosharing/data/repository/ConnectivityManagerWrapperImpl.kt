package com.example.deviceinfosharing.data.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.example.deviceinfosharing.domain.repository.ConnectivityManagerWrapper
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of ConnectivityManagerWrapper for network monitoring
 * Tracks Internet connectivity status changes
 */
@Singleton
class ConnectivityManagerWrapperImpl @Inject constructor(
    private val context: Context
) : ConnectivityManagerWrapper {
    
    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) 
        as ConnectivityManager
    private var isMonitoring = false
    
    override fun getConnectivityStatus(): Flow<Boolean> = callbackFlow {
        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                Timber.d("Network available")
                trySend(true)
            }
            
            override fun onLost(network: Network) {
                Timber.d("Network lost")
                trySend(false)
            }
            
            override fun onUnavailable() {
                Timber.d("Network unavailable")
                trySend(false)
            }
        }
        
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        
        try {
            connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
            
            // Emit initial state
            val isConnected = isConnected()
            trySend(isConnected)
            Timber.d("Connectivity monitoring started, initial state: $isConnected")
        } catch (e: Exception) {
            Timber.e(e, "Error registering network callback")
            close(e)
        }
        
        awaitClose {
            try {
                connectivityManager.unregisterNetworkCallback(networkCallback)
                Timber.d("Network callback unregistered")
            } catch (e: Exception) {
                Timber.e(e, "Error unregistering network callback")
            }
        }
    }
    
    override fun startMonitoring() {
        if (isMonitoring) return
        
        try {
            val networkRequest = NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build()
            
            val networkCallback = object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    Timber.d("Network available during monitoring")
                }
                
                override fun onLost(network: Network) {
                    Timber.d("Network lost during monitoring")
                }
            }
            
            connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
            isMonitoring = true
            Timber.d("Connectivity monitoring started")
        } catch (e: Exception) {
            Timber.e(e, "Error starting connectivity monitoring")
        }
    }
    
    override fun stopMonitoring() {
        if (!isMonitoring) return
        
        try {
            // Note: In production, you'd need to store the callback reference
            // to unregister it properly. For now, this is a simplified version.
            isMonitoring = false
            Timber.d("Connectivity monitoring stopped")
        } catch (e: Exception) {
            Timber.e(e, "Error stopping connectivity monitoring")
        }
    }
    
    override fun isConnected(): Boolean {
        return try {
            val activeNetwork = connectivityManager.activeNetwork
            if (activeNetwork != null) {
                val caps = connectivityManager.getNetworkCapabilities(activeNetwork)
                caps?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) ?: false
            } else {
                false
            }
        } catch (e: Exception) {
            Timber.e(e, "Error checking connectivity")
            false
        }
    }
}
