package com.example.deviceinfosharing.data.local

import android.content.Context
import android.content.SharedPreferences
import androidx.test.core.app.ApplicationProvider
import com.google.gson.Gson
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

/**
 * Integration tests for SharedPreferencesManager using real SharedPreferences
 * This test runs with Robolectric to test against real Android components
 */
@RunWith(RobolectricTestRunner::class)
class SharedPreferencesManagerIntegrationTest {

    private lateinit var context: Context
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var gson: Gson
    private lateinit var manager: SharedPreferencesManager

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        sharedPreferences = context.getSharedPreferences("test_prefs", Context.MODE_PRIVATE)
        gson = Gson()
        manager = SharedPreferencesManager(sharedPreferences, gson)

        // Clean up before each test
        sharedPreferences.edit().clear().apply()
    }

    // Device ID Integration Tests
    @Test
    fun `test saveAndLoad deviceId with real SharedPreferences`() {
        val deviceId = "device-id-12345"

        manager.saveDeviceId(deviceId)
        val loadedId = manager.loadDeviceId()

        assert(loadedId == deviceId) { "Expected $deviceId, got $loadedId" }
    }

    @Test
    fun `test deviceId persists across multiple loads`() {
        val deviceId = "unique-device-id-xyz"

        manager.saveDeviceId(deviceId)
        val firstLoad = manager.loadDeviceId()
        val secondLoad = manager.loadDeviceId()

        assert(firstLoad == deviceId)
        assert(secondLoad == deviceId)
        assert(firstLoad == secondLoad)
    }

    // Connected Devices Integration Tests
    @Test
    fun `test saveAndLoad connectedDevices with real SharedPreferences`() {
        val devices = listOf("device-1", "device-2", "device-3")

        manager.saveConnectedDevices(devices)
        val loaded = manager.loadConnectedDevices()

        assert(loaded.size == 3)
        assert(loaded == devices)
        assert(loaded.containsAll(devices))
    }

    @Test
    fun `test saveAndLoad empty connectedDevices list`() {
        val devices = emptyList<String>()

        manager.saveConnectedDevices(devices)
        val loaded = manager.loadConnectedDevices()

        assert(loaded.isEmpty())
    }

    @Test
    fun `test loadConnectedDevices handles missing data gracefully`() {
        val loaded = manager.loadConnectedDevices()

        assert(loaded.isEmpty())
    }

    @Test
    fun `test saveAndLoad large connectedDevices list`() {
        val devices = (1..100).map { "device-$it" }.toList()

        manager.saveConnectedDevices(devices)
        val loaded = manager.loadConnectedDevices()

        assert(loaded.size == 100)
        assert(loaded == devices)
    }

    // Device Name Integration Tests
    @Test
    fun `test saveAndLoad single device name`() {
        val deviceId = "device-1"
        val deviceName = "My Phone"

        manager.saveDeviceName(deviceId, deviceName)
        val loaded = manager.loadDeviceName(deviceId)

        assert(loaded == deviceName)
    }

    @Test
    fun `test saveAndLoad multiple device names`() {
        manager.saveDeviceName("device-1", "Phone A")
        manager.saveDeviceName("device-2", "Phone B")
        manager.saveDeviceName("device-3", "Tablet C")

        assert(manager.loadDeviceName("device-1") == "Phone A")
        assert(manager.loadDeviceName("device-2") == "Phone B")
        assert(manager.loadDeviceName("device-3") == "Tablet C")
    }

    @Test
    fun `test updateDeviceName overwrites previous name`() {
        val deviceId = "device-1"

        manager.saveDeviceName(deviceId, "Old Name")
        assert(manager.loadDeviceName(deviceId) == "Old Name")

        manager.saveDeviceName(deviceId, "New Name")
        assert(manager.loadDeviceName(deviceId) == "New Name")
    }

    @Test
    fun `test loadDeviceName returns empty for non-existent device`() {
        val loaded = manager.loadDeviceName("non-existent-device")

        assert(loaded == "")
    }

    // Device Names Map Integration Tests
    @Test
    fun `test loadDeviceNamesMap returns complete map`() {
        manager.saveDeviceName("device-1", "Phone A")
        manager.saveDeviceName("device-2", "Phone B")
        manager.saveDeviceName("device-3", "Phone C")

        val map = manager.loadDeviceNamesMap()

        assert(map.size == 3)
        assert(map["device-1"] == "Phone A")
        assert(map["device-2"] == "Phone B")
        assert(map["device-3"] == "Phone C")
    }

    @Test
    fun `test loadDeviceNamesMap handles empty map`() {
        val map = manager.loadDeviceNamesMap()

        assert(map.isEmpty())
    }

    // Remove Device Name Integration Test
    @Test
    fun `test removeDeviceName removes device from map`() {
        manager.saveDeviceName("device-1", "Phone A")
        manager.saveDeviceName("device-2", "Phone B")

        manager.removeDeviceName("device-1")

        assert(manager.loadDeviceName("device-1") == "")
        assert(manager.loadDeviceName("device-2") == "Phone B")
    }

    @Test
    fun `test removeDeviceName handles non-existent device gracefully`() {
        manager.saveDeviceName("device-1", "Phone A")

        manager.removeDeviceName("non-existent")

        assert(manager.loadDeviceName("device-1") == "Phone A")
    }

    // Clear All Integration Test
    @Test
    fun `test clearAll removes all data`() {
        manager.saveDeviceId("device-id-123")
        manager.saveConnectedDevices(listOf("device-1", "device-2"))
        manager.saveDeviceName("device-1", "Phone A")

        manager.clearAll()

        assert(manager.loadDeviceId() == "")
        assert(manager.loadConnectedDevices().isEmpty())
        assert(manager.loadDeviceName("device-1") == "")
    }

    // Complex Scenario Tests
    @Test
    fun `test complete workflow with multiple operations`() {
        // Setup
        val deviceId = "main-device-id-xyz"
        val connectedDevices = listOf("device-1", "device-2")

        // Save all data
        manager.saveDeviceId(deviceId)
        manager.saveConnectedDevices(connectedDevices)
        manager.saveDeviceName("device-1", "Friend's Phone")
        manager.saveDeviceName("device-2", "Family Member's Tablet")

        // Verify all data was saved
        assert(manager.loadDeviceId() == deviceId)
        assert(manager.loadConnectedDevices() == connectedDevices)
        assert(manager.loadDeviceName("device-1") == "Friend's Phone")
        assert(manager.loadDeviceName("device-2") == "Family Member's Tablet")

        // Update a device name
        manager.saveDeviceName("device-1", "Best Friend's Phone")
        assert(manager.loadDeviceName("device-1") == "Best Friend's Phone")

        // Remove a device
        manager.removeDeviceName("device-2")
        assert(manager.loadDeviceName("device-2") == "")
        assert(manager.loadDeviceName("device-1") == "Best Friend's Phone") // Other data unaffected
    }

    @Test
    fun `test special characters in device names`() {
        val specialNames = mapOf(
            "device-1" to "张三的手机",
            "device-2" to "João's Phone",
            "device-3" to "مريم's Tablet",
            "device-4" to "Device with émojis 📱🚀",
            "device-5" to "Device with \"quotes\" and 'apostrophes'"
        )

        specialNames.forEach { (id, name) ->
            manager.saveDeviceName(id, name)
        }

        specialNames.forEach { (id, name) ->
            val loaded = manager.loadDeviceName(id)
            assert(loaded == name) { "Expected '$name', got '$loaded'" }
        }
    }

    @Test
    fun `test large device name map`() {
        val deviceCount = 50
        for (i in 1..deviceCount) {
            manager.saveDeviceName("device-$i", "Device $i")
        }

        val map = manager.loadDeviceNamesMap()
        assert(map.size == deviceCount)

        for (i in 1..deviceCount) {
            assert(map["device-$i"] == "Device $i")
        }
    }

    @Test
    fun `test rapid successive saves`() {
        repeat(10) { index ->
            manager.saveDeviceId("device-$index")
            manager.saveConnectedDevices(listOf("device-1", "device-2", "device-3"))
            manager.saveDeviceName("device-$index", "Name $index")
        }

        // Last save should win
        assert(manager.loadDeviceId() == "device-9")
        assert(manager.loadConnectedDevices() == listOf("device-1", "device-2", "device-3"))
        assert(manager.loadDeviceName("device-9") == "Name 9")
    }
}
