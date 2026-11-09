package com.example.deviceinfosharing.domain.repository

import com.example.deviceinfosharing.domain.model.BatteryData
import kotlinx.coroutines.flow.Flow

/**
 * Interface for battery monitoring operations
 */
interface BatteryMonitor {
    
    /**
     * Get battery status updates as a Flow
     * @return Flow of BatteryData
     */
    fun getBatteryStatus(): Flow<BatteryData>
    
    /**
     * Start monitoring battery changes
     */
    fun startMonitoring()
    
    /**
     * Stop monitoring battery changes
     */
    fun stopMonitoring()
}
