package com.example.deviceinfosharing.data.local

import android.content.SharedPreferences
import com.google.gson.Gson
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

/**
 * Unit tests for SharedPreferencesManager
 */
class SharedPreferencesManagerTest {

    @Mock
    private lateinit var mockSharedPreferences: SharedPreferences

    @Mock
    private lateinit var mockEditor: SharedPreferences.Editor

    private lateinit var gson: Gson
    private lateinit var manager: SharedPreferencesManager

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        gson = Gson()

        // Setup mock editor to return itself for chaining
        whenever(mockSharedPreferences.edit()).thenReturn(mockEditor)
        whenever(mockEditor.putString(any(), any())).thenReturn(mockEditor)

        manager = SharedPreferencesManager(mockSharedPreferences, gson)
    }

    // Device ID Tests
    @Test
    fun `test saveDeviceId saves to SharedPreferences`() {
        val deviceId = "test-device-id-12345"

        manager.saveDeviceId(deviceId)

        val captor = argumentCaptor<String>()
        verify(mockEditor).putString(captor.capture(), captor.capture())
        assert(captor.allValues.contains(deviceId))
        verify(mockEditor).apply()
    }

    @Test
    fun `test loadDeviceId returns saved value`() {
        val deviceId = "test-device-id-12345"
        whenever(mockSharedPreferences.getString("device_id", ""))
            .thenReturn(deviceId)

        val result = manager.loadDeviceId()

        assert(result == deviceId)
    }

    @Test
    fun `test loadDeviceId returns empty string when not found`() {
        whenever(mockSharedPreferences.getString("device_id", ""))
            .thenReturn("")

        val result = manager.loadDeviceId()

        assert(result == "")
    }

    @Test
    fun `test loadDeviceId returns empty string when null`() {
        whenever(mockSharedPreferences.getString("device_id", ""))
            .thenReturn(null)

        val result = manager.loadDeviceId()

        assert(result == "")
    }

    // Connected Devices Tests
    @Test
    fun `test saveConnectedDevices saves JSON list to SharedPreferences`() {
        val connectedDevices = listOf("device-1", "device-2", "device-3")

        manager.saveConnectedDevices(connectedDevices)

        verify(mockEditor).putString(any(), any())
        verify(mockEditor).apply()
    }

    @Test
    fun `test loadConnectedDevices returns saved list`() {
        val connectedDevices = listOf("device-1", "device-2", "device-3")
        val jsonString = gson.toJson(connectedDevices)
        whenever(mockSharedPreferences.getString("connected_devices", "[]"))
            .thenReturn(jsonString)

        val result = manager.loadConnectedDevices()

        assert(result == connectedDevices)
        assert(result.size == 3)
    }

    @Test
    fun `test loadConnectedDevices returns empty list when not found`() {
        whenever(mockSharedPreferences.getString("connected_devices", "[]"))
            .thenReturn("[]")

        val result = manager.loadConnectedDevices()

        assert(result.isEmpty())
    }

    @Test
    fun `test loadConnectedDevices returns empty list on invalid JSON`() {
        whenever(mockSharedPreferences.getString("connected_devices", "[]"))
            .thenReturn("invalid json")

        val result = manager.loadConnectedDevices()

        assert(result.isEmpty())
    }

    @Test
    fun `test loadConnectedDevices returns empty list when null`() {
        whenever(mockSharedPreferences.getString("connected_devices", "[]"))
            .thenReturn(null)

        val result = manager.loadConnectedDevices()

        assert(result.isEmpty())
    }

    // Device Name Tests
    @Test
    fun `test saveDeviceName saves to device names map`() {
        val deviceId = "device-1"
        val deviceName = "My Phone"
        whenever(mockSharedPreferences.getString("device_names_map", "{}"))
            .thenReturn("{}")

        manager.saveDeviceName(deviceId, deviceName)

        verify(mockEditor).putString(any(), any())
        verify(mockEditor).apply()
    }

    @Test
    fun `test loadDeviceName returns saved name`() {
        val deviceId = "device-1"
        val deviceName = "My Phone"
        val namesMap = mapOf(deviceId to deviceName)
        val jsonString = gson.toJson(namesMap)
        whenever(mockSharedPreferences.getString("device_names_map", "{}"))
            .thenReturn(jsonString)

        val result = manager.loadDeviceName(deviceId)

        assert(result == deviceName)
    }

    @Test
    fun `test loadDeviceName returns empty string when device not found`() {
        val namesMap = mapOf("device-1" to "My Phone")
        val jsonString = gson.toJson(namesMap)
        whenever(mockSharedPreferences.getString("device_names_map", "{}"))
            .thenReturn(jsonString)

        val result = manager.loadDeviceName("device-2")

        assert(result == "")
    }

    @Test
    fun `test loadDeviceName returns empty string when map is empty`() {
        whenever(mockSharedPreferences.getString("device_names_map", "{}"))
            .thenReturn("{}")

        val result = manager.loadDeviceName("device-1")

        assert(result == "")
    }

    @Test
    fun `test loadDeviceName returns empty string on invalid JSON`() {
        whenever(mockSharedPreferences.getString("device_names_map", "{}"))
            .thenReturn("invalid json")

        val result = manager.loadDeviceName("device-1")

        assert(result == "")
    }

    // Device Names Map Tests
    @Test
    fun `test loadDeviceNamesMap returns complete map`() {
        val namesMap = mapOf(
            "device-1" to "Phone A",
            "device-2" to "Phone B",
            "device-3" to "Phone C"
        )
        val jsonString = gson.toJson(namesMap)
        whenever(mockSharedPreferences.getString("device_names_map", "{}"))
            .thenReturn(jsonString)

        val result = manager.loadDeviceNamesMap()

        assert(result.size == 3)
        assert(result["device-1"] == "Phone A")
        assert(result["device-2"] == "Phone B")
        assert(result["device-3"] == "Phone C")
    }

    @Test
    fun `test loadDeviceNamesMap returns empty map when not found`() {
        whenever(mockSharedPreferences.getString("device_names_map", "{}"))
            .thenReturn("{}")

        val result = manager.loadDeviceNamesMap()

        assert(result.isEmpty())
    }

    @Test
    fun `test loadDeviceNamesMap returns empty map on invalid JSON`() {
        whenever(mockSharedPreferences.getString("device_names_map", "{}"))
            .thenReturn("invalid json")

        val result = manager.loadDeviceNamesMap()

        assert(result.isEmpty())
    }

    // Remove Device Name Test
    @Test
    fun `test removeDeviceName removes device from map`() {
        val namesMap = mapOf(
            "device-1" to "Phone A",
            "device-2" to "Phone B"
        )
        val jsonString = gson.toJson(namesMap)
        whenever(mockSharedPreferences.getString("device_names_map", "{}"))
            .thenReturn(jsonString)

        manager.removeDeviceName("device-1")

        verify(mockEditor).putString(any(), any())
        verify(mockEditor).apply()
    }

    // Clear All Test
    @Test
    fun `test clearAll clears all preferences`() {
        manager.clearAll()

        verify(mockEditor).clear()
        verify(mockEditor).apply()
    }

    // Multiple Operations Test
    @Test
    fun `test multiple save operations work correctly`() {
        whenever(mockSharedPreferences.getString("connected_devices", "[]"))
            .thenReturn("[]")
        whenever(mockSharedPreferences.getString("device_names_map", "{}"))
            .thenReturn("{}")

        val deviceId = "test-device-123"
        val connectedDevices = listOf("device-1", "device-2")

        manager.saveDeviceId(deviceId)
        manager.saveConnectedDevices(connectedDevices)
        manager.saveDeviceName("device-1", "Phone A")

        // Verify all operations called apply
        verify(mockEditor).apply()
    }
}
