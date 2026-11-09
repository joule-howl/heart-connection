package com.example.deviceinfosharing.data.repository

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import com.example.deviceinfosharing.domain.model.BatteryData
import com.example.deviceinfosharing.domain.repository.BatteryMonitor
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of BatteryMonitor using Android BroadcastReceiver
 * Monitors battery status changes and emits updates
 */
@Singleton
class BatteryMonitorImpl @Inject constructor(
    private val context: Context
) : BatteryMonitor {
    
    private var isMonitoring = false
    private var batteryReceiver: BroadcastReceiver? = null
    
    override fun getBatteryStatus(): Flow<BatteryData> = callbackFlow {
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent == null) return
                
                try {
                    val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
                    val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 100)
                    val percentage = if (scale > 0) (level * 100) / scale else 0
                    
                    val status = intent.getIntExtra(
                        BatteryManager.EXTRA_STATUS,
                        BatteryManager.BATTERY_STATUS_UNKNOWN
                    )
                    val statusString = when (status) {
                        BatteryManager.BATTERY_STATUS_CHARGING -> "Charging"
                        BatteryManager.BATTERY_STATUS_DISCHARGING -> "Discharging"
                        BatteryManager.BATTERY_STATUS_FULL -> "Full"
                        BatteryManager.BATTERY_STATUS_NOT_CHARGING -> "Not Charging"
                        else -> "Unknown"
                    }
                    
                    val health = intent.getIntExtra(
                        BatteryManager.EXTRA_HEALTH,
                        BatteryManager.BATTERY_HEALTH_UNKNOWN
                    )
                    val healthString = when (health) {
                        BatteryManager.BATTERY_HEALTH_GOOD -> "Good"
                        BatteryManager.BATTERY_HEALTH_OVERHEAT -> "Warning"
                        BatteryManager.BATTERY_HEALTH_COLD -> "Warning"
                        BatteryManager.BATTERY_HEALTH_DEAD -> "Critical"
                        BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE -> "Warning"
                        else -> "Unknown"
                    }
                    
                    val batteryData = BatteryData(
                        percentage = percentage,
                        status = statusString,
                        health = healthString
                    )
                    
                    trySend(batteryData)
                    Timber.d("Battery update: ${batteryData.percentage}% - ${batteryData.status}")
                } catch (e: Exception) {
                    Timber.e(e, "Error processing battery intent")
                    close(e)
                }
            }
        }
        
        val intentFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        context.registerReceiver(receiver, intentFilter, Context.RECEIVER_EXPORTED)
        
        awaitClose {
            try {
                context.unregisterReceiver(receiver)
                Timber.d("Battery receiver unregistered")
            } catch (e: IllegalArgumentException) {
                Timber.e(e, "Battery receiver not registered")
            }
        }
    }
    
    override fun startMonitoring() {
        if (isMonitoring) return
        
        try {
            batteryReceiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context?, intent: Intent?) {
                    Timber.d("Battery monitoring active")
                }
            }
            
            val intentFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
            context.registerReceiver(batteryReceiver, intentFilter, Context.RECEIVER_EXPORTED)
            isMonitoring = true
            Timber.d("Battery monitoring started")
        } catch (e: Exception) {
            Timber.e(e, "Error starting battery monitoring")
        }
    }
    
    override fun stopMonitoring() {
        if (!isMonitoring || batteryReceiver == null) return
        
        try {
            context.unregisterReceiver(batteryReceiver)
            isMonitoring = false
            Timber.d("Battery monitoring stopped")
        } catch (e: IllegalArgumentException) {
            Timber.e(e, "Error stopping battery monitoring")
        }
    }
}
