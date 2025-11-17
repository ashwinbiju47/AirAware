package com.example.airaware.ui.home.airquality

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun AirQualitySection(
    viewModel: AirQualityViewModel = viewModel()
) {
    val aqi by viewModel.aqi.collectAsState()
    var expanded by remember { mutableStateOf(false) }
    var selectedLocation by remember { mutableStateOf(presetLocations.first { it.name == "Delhi" }) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Dropdown selector
        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(selectedLocation?.name ?: "Select a Location")
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            presetLocations.forEach { location ->
                DropdownMenuItem(
                    text = { Text(location.name) },
                    onClick = {
                        expanded = false
                        selectedLocation = location
                        viewModel.loadAQI(location.lat, location.lon)
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Air Quality Index (PM2.5)",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))

                if (aqi != null) {
                    Text(
                        text = "${aqi} µg/m³",
                        style = MaterialTheme.typography.headlineMedium
                    )
                } else {
                    Text("No data available")
                }
            }
        }
    }
}
