package com.example.deviceinfosharing.data.local

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

/**
 * SharedPreferencesManager
 *
 * A wrapper class for managing SharedPreferences operations.
 * Handles serialization/deserialization of JSON data using Gson.
 * Provides centralized key management and error handling.
 */
@Singleton
class SharedPreferencesManager @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson
) {

    companion object {
        // Key constants
        private const val KEY_DEVICE_ID = "device_id"
        private const val KEY_DEVICE_NAME = "device_name"
        private const val KEY_CONNECTED_DEVICES = "connected_devices"
        private const val KEY_DEVICE_NAMES_MAP = "device_names_map"

        // Default values
        private const val DEFAULT_EMPTY_STRING = ""
        private const val DEFAULT_EMPTY_LIST = "[]"
        private const val DEFAULT_EMPTY_MAP = "{}"
    }

    /**
     * Saves device ID to SharedPreferences
     * @param deviceId The unique device identifier
     */
    fun saveDeviceId(deviceId: String) {
        try {
            sharedPreferences.edit().putString(KEY_DEVICE_ID, deviceId).apply()
            Timber.d("Device ID saved: $deviceId")
        } catch (e: Exception) {
            Timber.e(e, "Error saving device ID")
        }
    }

    /**
     * Loads device ID from SharedPreferences
     * @return The saved device ID, or empty string if not found
     */
    fun loadDeviceId(): String {
        return try {
            val deviceId = sharedPreferences.getString(KEY_DEVICE_ID, DEFAULT_EMPTY_STRING)
                ?: DEFAULT_EMPTY_STRING
            Timber.d("Device ID loaded: $deviceId")
            deviceId
        } catch (e: Exception) {
            Timber.e(e, "Error loading device ID")
            DEFAULT_EMPTY_STRING
        }
    }

    /**
     * Saves connected devices list to SharedPreferences
     * @param connectedDevices List of connected device IDs
     */
    fun saveConnectedDevices(connectedDevices: List<String>) {
        try {
            val jsonString = gson.toJson(connectedDevices)
            sharedPreferences.edit().putString(KEY_CONNECTED_DEVICES, jsonString).apply()
            Timber.d("Connected devices saved: ${connectedDevices.size} devices")
        } catch (e: Exception) {
            Timber.e(e, "Error saving connected devices")
        }
    }

    /**
     * Loads connected devices list from SharedPreferences
     * @return List of connected device IDs, or empty list if not found or invalid JSON
     */
    fun loadConnectedDevices(): List<String> {
        return try {
            val jsonString = sharedPreferences.getString(KEY_CONNECTED_DEVICES, DEFAULT_EMPTY_LIST)
                ?: DEFAULT_EMPTY_LIST

            if (jsonString.isEmpty() || jsonString == DEFAULT_EMPTY_LIST) {
                Timber.d("No connected devices found")
                return emptyList()
            }

            val devices = gson.fromJson(jsonString, Array<String>::class.java).toList()
            Timber.d("Connected devices loaded: ${devices.size} devices")
            devices
        } catch (e: JsonSyntaxException) {
            Timber.e(e, "Invalid JSON for connected devices")
            emptyList()
        } catch (e: Exception) {
            Timber.e(e, "Error loading connected devices")
            emptyList()
        }
    }

    /**
     * Saves device name to SharedPreferences
     * @param deviceId The device identifier
     * @param deviceName The display name for the device
     */
    fun saveDeviceName(deviceId: String, deviceName: String) {
        try {
            // Load existing map
            val currentMap = loadDeviceNamesMap().toMutableMap()
            currentMap[deviceId] = deviceName
            
            // Save updated map
            val jsonString = gson.toJson(currentMap)
            sharedPreferences.edit().putString(KEY_DEVICE_NAMES_MAP, jsonString).apply()
            Timber.d("Device name saved for $deviceId: $deviceName")
        } catch (e: Exception) {
            Timber.e(e, "Error saving device name for $deviceId")
        }
    }

    /**
     * Loads device name from SharedPreferences
     * @param deviceId The device identifier
     * @return The saved device name, or empty string if not found
     */
    fun loadDeviceName(deviceId: String): String {
        return try {
            val namesMap = loadDeviceNamesMap()
            val deviceName = namesMap[deviceId] ?: DEFAULT_EMPTY_STRING
            Timber.d("Device name loaded for $deviceId: $deviceName")
            deviceName
        } catch (e: Exception) {
            Timber.e(e, "Error loading device name for $deviceId")
            DEFAULT_EMPTY_STRING
        }
    }

    /**
     * Loads the entire device names map from SharedPreferences
     * @return Map of device IDs to device names
     */
    fun loadDeviceNamesMap(): Map<String, String> {
        return try {
            val jsonString = sharedPreferences.getString(KEY_DEVICE_NAMES_MAP, DEFAULT_EMPTY_MAP)
                ?: DEFAULT_EMPTY_MAP

            if (jsonString.isEmpty() || jsonString == DEFAULT_EMPTY_MAP) {
                Timber.d("No device names map found")
                return emptyMap()
            }

            val namesMap = gson.fromJson(jsonString, Map::class.java)
                .mapKeys { it.key.toString() }
                .mapValues { it.value.toString() }
            Timber.d("Device names map loaded: ${namesMap.size} entries")
            namesMap
        } catch (e: JsonSyntaxException) {
            Timber.e(e, "Invalid JSON for device names map")
            emptyMap()
        } catch (e: Exception) {
            Timber.e(e, "Error loading device names map")
            emptyMap()
        }
    }

    /**
     * Removes a device name from the map
     * @param deviceId The device identifier to remove
     */
    fun removeDeviceName(deviceId: String) {
        try {
            val currentMap = loadDeviceNamesMap().toMutableMap()
            currentMap.remove(deviceId)
            
            val jsonString = gson.toJson(currentMap)
            sharedPreferences.edit().putString(KEY_DEVICE_NAMES_MAP, jsonString).apply()
            Timber.d("Device name removed for $deviceId")
        } catch (e: Exception) {
            Timber.e(e, "Error removing device name for $deviceId")
        }
    }

    /**
     * Clears all stored preferences
     */
    fun clearAll() {
        try {
            sharedPreferences.edit().clear().apply()
            Timber.d("All preferences cleared")
        } catch (e: Exception) {
            Timber.e(e, "Error clearing preferences")
        }
    }
}
