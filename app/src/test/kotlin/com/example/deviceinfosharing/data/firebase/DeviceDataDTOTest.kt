package com.example.deviceinfosharing.data.firebase

import com.google.gson.Gson
import com.example.deviceinfosharing.domain.model.DeviceData
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for DeviceDataDTO
 * 
 * Tests verify:
 * - Gson serialization to JSON
 * - Gson deserialization from JSON
 * - Conversion between DTO and Domain Model
 * - Default values are properly set
 * - @SerializedName annotations work correctly
 */
class DeviceDataDTOTest {
    
    private lateinit var gson: Gson

    @Before
    fun setUp() {
        gson = Gson()
    }

    // ========== Serialization Tests (DTO -> JSON) ==========

    /**
     * Test that DeviceDataDTO is correctly serialized to JSON with @SerializedName fields
     */
    @Test
    fun testSerializeDeviceDataDTOToJson() {
        // Arrange
        val dto = DeviceDataDTO(
            deviceId = "device-123",
            deviceName = "My Phone",
            timestamp = 1704067200000L,
            temperature = 28.5,
            location = "Hà Nội, Việt Nam",
            weatherDescription = "Partly Cloudy",
            batteryPercentage = 85,
            connectionStatus = "Online"
        )

        // Act
        val json = gson.toJson(dto)

        // Assert
        assertNotNull(json)
        assertTrue(json.contains("\"deviceId\":\"device-123\""))
        assertTrue(json.contains("\"deviceName\":\"My Phone\""))
        assertTrue(json.contains("\"timestamp\":1704067200000"))
        assertTrue(json.contains("\"temperature\":28.5"))
        assertTrue(json.contains("\"location\":\"Hà Nội, Việt Nam\""))
        assertTrue(json.contains("\"weatherDescription\":\"Partly Cloudy\""))
        assertTrue(json.contains("\"batteryPercentage\":85"))
        assertTrue(json.contains("\"connectionStatus\":\"Online\""))
    }

    /**
     * Test that default values are properly serialized
     */
    @Test
    fun testSerializeWithDefaultValues() {
        // Arrange
        val dto = DeviceDataDTO()

        // Act
        val json = gson.toJson(dto)

        // Assert
        assertNotNull(json)
        assertTrue(json.contains("\"deviceId\":\"\""))
        assertTrue(json.contains("\"deviceName\":\"\""))
        assertTrue(json.contains("\"timestamp\":0"))
        assertTrue(json.contains("\"temperature\":0.0"))
        assertTrue(json.contains("\"location\":\"\""))
        assertTrue(json.contains("\"weatherDescription\":\"\""))
        assertTrue(json.contains("\"batteryPercentage\":0"))
        assertTrue(json.contains("\"connectionStatus\":\"Offline\""))
    }

    /**
     * Test that special characters in values are properly escaped in JSON
     */
    @Test
    fun testSerializeWithSpecialCharacters() {
        // Arrange
        val dto = DeviceDataDTO(
            deviceId = "device-\"123\"",
            deviceName = "User's Phone",
            timestamp = 1704067200000L,
            temperature = 25.0,
            location = "123 \"Main\" Street",
            weatherDescription = "Rainy (50%)",
            batteryPercentage = 50,
            connectionStatus = "Online"
        )

        // Act
        val json = gson.toJson(dto)

        // Assert
        assertNotNull(json)
        val deserializedDto = gson.fromJson(json, DeviceDataDTO::class.java)
        assertEquals("device-\"123\"", deserializedDto.deviceId)
        assertEquals("User's Phone", deserializedDto.deviceName)
        assertEquals("123 \"Main\" Street", deserializedDto.location)
    }

    // ========== Deserialization Tests (JSON -> DTO) ==========

    /**
     * Test that JSON is correctly deserialized to DeviceDataDTO with @SerializedName fields
     */
    @Test
    fun testDeserializeJsonToDeviceDataDTO() {
        // Arrange
        val json = """{
            "deviceId": "device-456",
            "deviceName": "Dad's Samsung",
            "timestamp": 1704153600000,
            "temperature": 25.2,
            "location": "Hồ Chí Minh, Việt Nam",
            "weatherDescription": "Clear",
            "batteryPercentage": 45,
            "connectionStatus": "Online"
        }""".trimIndent()

        // Act
        val dto = gson.fromJson(json, DeviceDataDTO::class.java)

        // Assert
        assertNotNull(dto)
        assertEquals("device-456", dto.deviceId)
        assertEquals("Dad's Samsung", dto.deviceName)
        assertEquals(1704153600000L, dto.timestamp)
        assertEquals(25.2, dto.temperature, 0.01)
        assertEquals("Hồ Chí Minh, Việt Nam", dto.location)
        assertEquals("Clear", dto.weatherDescription)
        assertEquals(45, dto.batteryPercentage)
        assertEquals("Online", dto.connectionStatus)
    }

    /**
     * Test deserialization with missing optional fields uses default values
     */
    @Test
    fun testDeserializeWithMissingFields() {
        // Arrange - JSON with only required fields
        val json = """{
            "deviceId": "device-789"
        }""".trimIndent()

        // Act
        val dto = gson.fromJson(json, DeviceDataDTO::class.java)

        // Assert
        assertNotNull(dto)
        assertEquals("device-789", dto.deviceId)
        assertEquals("", dto.deviceName)
        assertEquals(0L, dto.timestamp)
        assertEquals(0.0, dto.temperature, 0.01)
        assertEquals("", dto.location)
        assertEquals("", dto.weatherDescription)
        assertEquals(0, dto.batteryPercentage)
        assertEquals("Offline", dto.connectionStatus)
    }

    /**
     * Test deserialization with empty JSON object
     */
    @Test
    fun testDeserializeEmptyJson() {
        // Arrange
        val json = "{}"

        // Act
        val dto = gson.fromJson(json, DeviceDataDTO::class.java)

        // Assert
        assertNotNull(dto)
        assertEquals("", dto.deviceId)
        assertEquals("", dto.deviceName)
        assertEquals(0L, dto.timestamp)
        assertEquals(0.0, dto.temperature, 0.01)
        assertEquals("", dto.location)
        assertEquals("", dto.weatherDescription)
        assertEquals(0, dto.batteryPercentage)
        assertEquals("Offline", dto.connectionStatus)
    }

    /**
     * Test deserialization with null values (should use defaults if null)
     */
    @Test
    fun testDeserializeWithNullValues() {
        // Arrange
        val json = """{
            "deviceId": null,
            "deviceName": null,
            "timestamp": null,
            "temperature": null,
            "location": null,
            "weatherDescription": null,
            "batteryPercentage": null,
            "connectionStatus": null
        }""".trimIndent()

        // Act
        val dto = gson.fromJson(json, DeviceDataDTO::class.java)

        // Assert
        assertNotNull(dto)
        assertNull(dto.deviceId) // null values are preserved as null by Gson
        assertNull(dto.deviceName)
    }

    /**
     * Test deserialization with Unicode characters
     */
    @Test
    fun testDeserializeWithUnicodeCharacters() {
        // Arrange
        val json = """{
            "deviceId": "device-unicode",
            "deviceName": "Điện thoại của tôi 📱",
            "timestamp": 1704067200000,
            "temperature": 27.8,
            "location": "Hà Nội - Thành phố Hồ Chí Minh 🇻🇳",
            "weatherDescription": "Nắng - ☀️ Mây đen ⛅",
            "batteryPercentage": 92,
            "connectionStatus": "Online"
        }""".trimIndent()

        // Act
        val dto = gson.fromJson(json, DeviceDataDTO::class.java)

        // Assert
        assertNotNull(dto)
        assertEquals("Điện thoại của tôi 📱", dto.deviceName)
        assertEquals("Hà Nội - Thành phố Hồ Chí Minh 🇻🇳", dto.location)
        assertEquals("Nắng - ☀️ Mây đen ⛅", dto.weatherDescription)
    }

    // ========== Round-trip Tests (Serialize -> Deserialize) ==========

    /**
     * Test round-trip: DTO -> JSON -> DTO should be identical
     */
    @Test
    fun testRoundTripSerializationDeserialization() {
        // Arrange
        val originalDto = DeviceDataDTO(
            deviceId = "device-round-trip",
            deviceName = "Round Trip Phone",
            timestamp = 1704240000000L,
            temperature = 32.1,
            location = "Saigon Tower",
            weatherDescription = "Hot and Humid",
            batteryPercentage = 67,
            connectionStatus = "Online"
        )

        // Act
        val json = gson.toJson(originalDto)
        val deserializedDto = gson.fromJson(json, DeviceDataDTO::class.java)

        // Assert
        assertEquals(originalDto.deviceId, deserializedDto.deviceId)
        assertEquals(originalDto.deviceName, deserializedDto.deviceName)
        assertEquals(originalDto.timestamp, deserializedDto.timestamp)
        assertEquals(originalDto.temperature, deserializedDto.temperature, 0.01)
        assertEquals(originalDto.location, deserializedDto.location)
        assertEquals(originalDto.weatherDescription, deserializedDto.weatherDescription)
        assertEquals(originalDto.batteryPercentage, deserializedDto.batteryPercentage)
        assertEquals(originalDto.connectionStatus, deserializedDto.connectionStatus)
    }

    /**
     * Test multiple round-trips maintain data integrity
     */
    @Test
    fun testMultipleRoundTrips() {
        // Arrange
        val originalDto = DeviceDataDTO(
            deviceId = "device-multi",
            deviceName = "Multi Round Trip",
            timestamp = 1704326400000L,
            temperature = 29.5,
            location = "Central Park",
            weatherDescription = "Scattered Showers",
            batteryPercentage = 71,
            connectionStatus = "Online"
        )

        // Act - perform 3 round trips
        var currentDto = originalDto
        repeat(3) {
            val json = gson.toJson(currentDto)
            currentDto = gson.fromJson(json, DeviceDataDTO::class.java)
        }

        // Assert
        assertEquals(originalDto.deviceId, currentDto.deviceId)
        assertEquals(originalDto.deviceName, currentDto.deviceName)
        assertEquals(originalDto.timestamp, currentDto.timestamp)
        assertEquals(originalDto.temperature, currentDto.temperature, 0.01)
        assertEquals(originalDto.location, currentDto.location)
        assertEquals(originalDto.weatherDescription, currentDto.weatherDescription)
        assertEquals(originalDto.batteryPercentage, currentDto.batteryPercentage)
        assertEquals(originalDto.connectionStatus, currentDto.connectionStatus)
    }

    // ========== Domain Model Conversion Tests ==========

    /**
     * Test conversion from DeviceDataDTO to Domain Model (toDomainModel)
     */
    @Test
    fun testToDomainModel() {
        // Arrange
        val dto = DeviceDataDTO(
            deviceId = "device-to-domain",
            deviceName = "Domain Test Phone",
            timestamp = 1704412800000L,
            temperature = 30.0,
            location = "District 1",
            weatherDescription = "Windy",
            batteryPercentage = 88,
            connectionStatus = "Online"
        )

        // Act
        val domainModel = dto.toDomainModel()

        // Assert
        assertNotNull(domainModel)
        assertEquals(dto.deviceId, domainModel.deviceId)
        assertEquals(dto.deviceName, domainModel.deviceName)
        assertEquals(dto.timestamp, domainModel.timestamp)
        assertEquals(dto.temperature, domainModel.temperature, 0.01)
        assertEquals(dto.location, domainModel.location)
        assertEquals(dto.weatherDescription, domainModel.weatherDescription)
        assertEquals(dto.batteryPercentage, domainModel.batteryPercentage)
        assertEquals(dto.connectionStatus, domainModel.connectionStatus)
    }

    /**
     * Test conversion from Domain Model to DeviceDataDTO (fromDomainModel)
     */
    @Test
    fun testFromDomainModel() {
        // Arrange
        val domainModel = DeviceData(
            deviceId = "device-from-domain",
            deviceName = "From Domain Phone",
            timestamp = 1704499200000L,
            temperature = 26.5,
            location = "Bình Thạnh",
            weatherDescription = "Overcast",
            batteryPercentage = 42,
            connectionStatus = "Offline"
        )

        // Act
        val dto = DeviceDataDTO.fromDomainModel(domainModel)

        // Assert
        assertNotNull(dto)
        assertEquals(domainModel.deviceId, dto.deviceId)
        assertEquals(domainModel.deviceName, dto.deviceName)
        assertEquals(domainModel.timestamp, dto.timestamp)
        assertEquals(domainModel.temperature, dto.temperature, 0.01)
        assertEquals(domainModel.location, dto.location)
        assertEquals(domainModel.weatherDescription, dto.weatherDescription)
        assertEquals(domainModel.batteryPercentage, dto.batteryPercentage)
        assertEquals(domainModel.connectionStatus, dto.connectionStatus)
    }

    /**
     * Test round-trip: Domain Model -> DTO -> JSON -> DTO -> Domain Model
     */
    @Test
    fun testDomainModelRoundTrip() {
        // Arrange
        val originalDomain = DeviceData(
            deviceId = "device-full-round",
            deviceName = "Full Round Trip",
            timestamp = 1704585600000L,
            temperature = 24.8,
            location = "Phú Nhuận",
            weatherDescription = "Sunny",
            batteryPercentage = 76,
            connectionStatus = "Online"
        )

        // Act
        val dto1 = DeviceDataDTO.fromDomainModel(originalDomain)
        val json = gson.toJson(dto1)
        val dto2 = gson.fromJson(json, DeviceDataDTO::class.java)
        val finalDomain = dto2.toDomainModel()

        // Assert
        assertEquals(originalDomain.deviceId, finalDomain.deviceId)
        assertEquals(originalDomain.deviceName, finalDomain.deviceName)
        assertEquals(originalDomain.timestamp, finalDomain.timestamp)
        assertEquals(originalDomain.temperature, finalDomain.temperature, 0.01)
        assertEquals(originalDomain.location, finalDomain.location)
        assertEquals(originalDomain.weatherDescription, finalDomain.weatherDescription)
        assertEquals(originalDomain.batteryPercentage, finalDomain.batteryPercentage)
        assertEquals(originalDomain.connectionStatus, finalDomain.connectionStatus)
    }

    // ========== Edge Cases and Boundary Tests ==========

    /**
     * Test with extreme temperature values
     */
    @Test
    fun testExtremeTemperatureValues() {
        // Arrange
        val dtoHot = DeviceDataDTO(
            deviceId = "device-hot",
            deviceName = "Hot Phone",
            timestamp = 1704067200000L,
            temperature = 55.0, // Very hot
            location = "Desert",
            weatherDescription = "Extreme Heat",
            batteryPercentage = 10,
            connectionStatus = "Online"
        )

        val dtoCold = DeviceDataDTO(
            deviceId = "device-cold",
            deviceName = "Cold Phone",
            timestamp = 1704067200000L,
            temperature = -40.0, // Very cold
            location = "Arctic",
            weatherDescription = "Freezing",
            batteryPercentage = 95,
            connectionStatus = "Online"
        )

        // Act & Assert
        val jsonHot = gson.toJson(dtoHot)
        val dtoHotDeserialized = gson.fromJson(jsonHot, DeviceDataDTO::class.java)
        assertEquals(55.0, dtoHotDeserialized.temperature, 0.01)

        val jsonCold = gson.toJson(dtoCold)
        val dtoColdDeserialized = gson.fromJson(jsonCold, DeviceDataDTO::class.java)
        assertEquals(-40.0, dtoColdDeserialized.temperature, 0.01)
    }

    /**
     * Test with boundary battery percentage values (0 and 100)
     */
    @Test
    fun testBatteryPercentageBoundaries() {
        // Arrange - Empty battery
        val dtoEmpty = DeviceDataDTO(
            deviceId = "device-empty",
            deviceName = "Empty Battery",
            timestamp = 1704067200000L,
            temperature = 25.0,
            location = "Somewhere",
            weatherDescription = "Unknown",
            batteryPercentage = 0,
            connectionStatus = "Online"
        )

        // Arrange - Full battery
        val dtoFull = DeviceDataDTO(
            deviceId = "device-full",
            deviceName = "Full Battery",
            timestamp = 1704067200000L,
            temperature = 25.0,
            location = "Somewhere",
            weatherDescription = "Unknown",
            batteryPercentage = 100,
            connectionStatus = "Online"
        )

        // Act & Assert
        val jsonEmpty = gson.toJson(dtoEmpty)
        val dtoEmptyDeserialized = gson.fromJson(jsonEmpty, DeviceDataDTO::class.java)
        assertEquals(0, dtoEmptyDeserialized.batteryPercentage)

        val jsonFull = gson.toJson(dtoFull)
        val dtoFullDeserialized = gson.fromJson(jsonFull, DeviceDataDTO::class.java)
        assertEquals(100, dtoFullDeserialized.batteryPercentage)
    }

    /**
     * Test with very long strings in location and device name
     */
    @Test
    fun testLongStringValues() {
        // Arrange
        val longDeviceName = "A".repeat(1000)
        val longLocation = "B".repeat(1000)
        val dto = DeviceDataDTO(
            deviceId = "device-long",
            deviceName = longDeviceName,
            timestamp = 1704067200000L,
            temperature = 25.0,
            location = longLocation,
            weatherDescription = "Normal",
            batteryPercentage = 50,
            connectionStatus = "Online"
        )

        // Act
        val json = gson.toJson(dto)
        val deserialized = gson.fromJson(json, DeviceDataDTO::class.java)

        // Assert
        assertEquals(longDeviceName, deserialized.deviceName)
        assertEquals(longLocation, deserialized.location)
    }

    /**
     * Test with very large timestamp values
     */
    @Test
    fun testLargeTimestampValues() {
        // Arrange
        val futureTimestamp = Long.MAX_VALUE
        val dto = DeviceDataDTO(
            deviceId = "device-future",
            deviceName = "Future Phone",
            timestamp = futureTimestamp,
            temperature = 20.0,
            location = "Future",
            weatherDescription = "Unknown",
            batteryPercentage = 50,
            connectionStatus = "Online"
        )

        // Act
        val json = gson.toJson(dto)
        val deserialized = gson.fromJson(json, DeviceDataDTO::class.java)

        // Assert
        assertEquals(futureTimestamp, deserialized.timestamp)
    }

    /**
     * Test connection status values
     */
    @Test
    fun testConnectionStatusValues() {
        // Arrange - Online
        val dtoOnline = DeviceDataDTO(
            deviceId = "device-online",
            deviceName = "Online Phone",
            timestamp = 1704067200000L,
            temperature = 25.0,
            location = "Office",
            weatherDescription = "Normal",
            batteryPercentage = 50,
            connectionStatus = "Online"
        )

        // Arrange - Offline
        val dtoOffline = DeviceDataDTO(
            deviceId = "device-offline",
            deviceName = "Offline Phone",
            timestamp = 1704067200000L,
            temperature = 25.0,
            location = "Office",
            weatherDescription = "Normal",
            batteryPercentage = 50,
            connectionStatus = "Offline"
        )

        // Act & Assert
        val jsonOnline = gson.toJson(dtoOnline)
        val dtoOnlineDeserialized = gson.fromJson(jsonOnline, DeviceDataDTO::class.java)
        assertEquals("Online", dtoOnlineDeserialized.connectionStatus)

        val jsonOffline = gson.toJson(dtoOffline)
        val dtoOfflineDeserialized = gson.fromJson(jsonOffline, DeviceDataDTO::class.java)
        assertEquals("Offline", dtoOfflineDeserialized.connectionStatus)
    }

    // ========== JSON Array Serialization Tests ==========

    /**
     * Test serialization of a list of DTOs (common Firebase use case)
     */
    @Test
    fun testSerializeListOfDTOs() {
        // Arrange
        val dtoList = listOf(
            DeviceDataDTO(
                deviceId = "device-1",
                deviceName = "Phone 1",
                timestamp = 1704067200000L,
                temperature = 25.0,
                location = "Location 1",
                weatherDescription = "Weather 1",
                batteryPercentage = 50,
                connectionStatus = "Online"
            ),
            DeviceDataDTO(
                deviceId = "device-2",
                deviceName = "Phone 2",
                timestamp = 1704153600000L,
                temperature = 26.0,
                location = "Location 2",
                weatherDescription = "Weather 2",
                batteryPercentage = 75,
                connectionStatus = "Online"
            )
        )

        // Act
        val json = gson.toJson(dtoList)

        // Assert
        assertNotNull(json)
        assertTrue(json.contains("device-1"))
        assertTrue(json.contains("device-2"))
        assertTrue(json.startsWith("["))
        assertTrue(json.endsWith("]"))
    }

    /**
     * Test deserialization of a list of DTOs
     */
    @Test
    fun testDeserializeListOfDTOs() {
        // Arrange
        val json = """[
            {
                "deviceId": "device-1",
                "deviceName": "Phone 1",
                "timestamp": 1704067200000,
                "temperature": 25.0,
                "location": "Location 1",
                "weatherDescription": "Weather 1",
                "batteryPercentage": 50,
                "connectionStatus": "Online"
            },
            {
                "deviceId": "device-2",
                "deviceName": "Phone 2",
                "timestamp": 1704153600000,
                "temperature": 26.0,
                "location": "Location 2",
                "weatherDescription": "Weather 2",
                "batteryPercentage": 75,
                "connectionStatus": "Online"
            }
        ]""".trimIndent()

        // Act
        val dtoList = gson.fromJson(json, Array<DeviceDataDTO>::class.java).toList()

        // Assert
        assertEquals(2, dtoList.size)
        assertEquals("device-1", dtoList[0].deviceId)
        assertEquals("device-2", dtoList[1].deviceId)
    }
}
