package com.example.deviceinfosharing.domain.model

/**
 * Domain Model representing battery status information.
 *
 * This data class encapsulates battery-related metrics and health indicators
 * without any framework dependencies. It provides essential battery information
 * for device health monitoring and power management decisions.
 *
 * @property percentage Current battery charge level as a percentage (0-100)
 * @property status Current charging status ("Charging", "Discharging", or "Full")
 * @property health Battery health indicator ("Good", "Warning", or "Critical")
 *
 * @author Device Info Sharing Team
 * @since 1.0
 */
data class BatteryData(
    val percentage: Int,
    val status: String,  // "Charging", "Discharging", "Full"
    val health: String   // "Good", "Warning", "Critical"
)
