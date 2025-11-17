package com.example.airaware.ui.home.airquality

data class PresetCountry(
    val id: Int,
    val name: String,
    val iso: String
)

val presetCountries = listOf(
    PresetCountry(id = 9, name = "India", iso = "IN"),
    PresetCountry(id = 155, name = "United States", iso = "US"),
    PresetCountry(id = 79, name = "United Kingdom", iso = "GB"),
    PresetCountry(id = 177, name = "Australia", iso = "AU"),
    PresetCountry(id = 50, name = "Germany", iso = "DE")
)
