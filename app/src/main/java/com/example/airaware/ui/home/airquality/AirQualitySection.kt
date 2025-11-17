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
    val countryData by viewModel.countryData.collectAsState()

    // Default: India (id=9)
    var expanded by remember { mutableStateOf(false) }
    var selectedCountry by remember { mutableStateOf(presetCountries.first { it.id == 9 }) }

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
                if (countryData != null) {
                    Text("Country: ${countryData!!.name}")
                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Available Parameters:")
                    Spacer(modifier = Modifier.height(4.dp))

                    countryData!!.parameters.take(6).forEach { param ->
                        Text("- ${param.name} (${param.units})")
                    }
                } else {
                    Text("No data available")
                }
            }
        }
    }
}
