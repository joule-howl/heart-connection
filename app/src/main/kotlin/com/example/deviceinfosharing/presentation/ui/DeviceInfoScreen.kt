package com.example.deviceinfosharing.presentation.ui

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * DeviceInfoScreen: Displays device information in a clean layout
 */
@Composable
fun DeviceInfoScreen(
    deviceId: String = "00000000-0000-0000-0000-000000000000",
    modifier: Modifier = Modifier
) {
    val deviceName = Build.DEVICE
    val androidVersion = Build.VERSION.RELEASE
    val manufacturer = Build.MANUFACTURER
    val model = Build.MODEL
    val brand = Build.BRAND
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "Device Info Sharing",
            fontSize = 28.sp,
            color = Color.White,
            modifier = Modifier.padding(bottom = 24.dp)
        )
        
        InfoCard(label = "Device ID", value = deviceId)
        InfoCard(label = "Device Name", value = deviceName)
        InfoCard(label = "Manufacturer", value = manufacturer)
        InfoCard(label = "Brand", value = brand)
        InfoCard(label = "Model", value = model)
        InfoCard(label = "Android Version", value = androidVersion)
        InfoCard(label = "SDK Level", value = Build.VERSION.SDK_INT.toString())
    }
}

/**
 * InfoCard: Reusable component for info display
 */
@Composable
fun InfoCard(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(bottom = 12.dp)
            .background(Color.DarkGray, shape = RoundedCornerShape(8.dp))
            .padding(12.dp)
    ) {
        Text(text = label, fontSize = 12.sp, color = Color.LightGray)
        Text(text = value, fontSize = 16.sp, color = Color.White, modifier = Modifier.padding(top = 4.dp))
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun DeviceInfoScreenPreview() {
    MaterialTheme {
        DeviceInfoScreen(deviceId = "a1b2c3d4-e5f6-7890-abcd-ef1234567890")
    }
}
