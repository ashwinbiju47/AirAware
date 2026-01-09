package com.example.airaware.ui.home.airquality

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun AirQualitySection(
    viewModel: AirQualityViewModel = viewModel()
) {
    val measurements by viewModel.measurements.collectAsState()
    val aqiResult by viewModel.aqiResult.collectAsState()
    val stationName by viewModel.stationName.collectAsState()
    val locationState by viewModel.locationState.collectAsState()
    val context = androidx.compose.ui.platform.LocalContext.current

    // Default: India (id=9)
    var expanded by remember { mutableStateOf(false) }
    var selectedCountry by remember { mutableStateOf(presetCountries.first { it.id == 9 }) }

    // Permission Launcher
    val permissionLauncher = androidx.activity.compose.rememberLauncherForActivityResult(
        androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[android.Manifest.permission.ACCESS_FINE_LOCATION] == true ||
            permissions[android.Manifest.permission.ACCESS_COARSE_LOCATION] == true
        ) {
            viewModel.detectLocation(context)
        } else {
            viewModel.setPermissionDenied()
        }
    }

    // Update selected country when location is detected
    LaunchedEffect(locationState) {
        if (locationState.status == LocationStatus.LocationDetected && locationState.country != null) {
            val matched = presetCountries.find { it.name == locationState.country }
            if (matched != null) {
                selectedCountry = matched
            }
        }
    }

    // On first load, fetch India
    LaunchedEffect(Unit) {
        viewModel.loadCountry(selectedCountry.id)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Dropdown selector
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.weight(1f)) {
                OutlinedButton(
                    onClick = { expanded = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(selectedCountry.name)
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    presetCountries.forEach { country ->
                        DropdownMenuItem(
                            text = { Text(country.name) },
                            onClick = {
                                expanded = false
                                selectedCountry = country
                                viewModel.loadCountry(country.id)
                            }
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.width(8.dp))

            IconButton(
                onClick = {
                    permissionLauncher.launch(
                        arrayOf(
                            android.Manifest.permission.ACCESS_FINE_LOCATION,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                    )
                }
            ) {
                if (locationState.status == LocationStatus.DetectingLocation) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(
                        imageVector = androidx.compose.material.icons.Icons.Default.LocationOn,
                        contentDescription = "Use My Location",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        if (locationState.status == LocationStatus.PermissionDenied) {
            Text(
                text = "Location access denied. Using default country.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 4.dp)
            )
        } else if (locationState.status == LocationStatus.Error) {
             Text(
                text = "Could not detect location.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 4.dp)
            )
        } else if (locationState.status == LocationStatus.LocationDetected) {
             Text(
                text = "Detected via GPS",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {

                Text(
                    text = "Air Quality Parameters",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(12.dp))
                if (stationName != null) {
                    Text("Station: $stationName")
                    Spacer(modifier = Modifier.height(8.dp))

                    if (measurements.isNotEmpty()) {
                        
                        // AQI Card
                        if (aqiResult != null) {
                            Card(
                                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                                colors = CardDefaults.cardColors(containerColor = aqiResult!!.color)
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "AQI: ${aqiResult!!.value}",
                                        style = MaterialTheme.typography.displayMedium,
                                        color = Color.White
                                    )
                                    Text(
                                        text = aqiResult!!.level,
                                        style = MaterialTheme.typography.headlineSmall,
                                        color = Color.White
                                    )
                                    Text(
                                        text = aqiResult!!.message,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color.White,
                                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                    )
                                }
                            }
                        }

                        Text("Available Parameters:")
                        Spacer(modifier = Modifier.height(4.dp))

                        measurements.forEach { m ->
                            Text("- ${m.parameter}: ${m.value} ${m.unit}")
                        }
                    } else {
                        Text("No recent data for this station.")
                    }
                } else {
                    Text("No data available")
                }
            }
        }
    }
}
