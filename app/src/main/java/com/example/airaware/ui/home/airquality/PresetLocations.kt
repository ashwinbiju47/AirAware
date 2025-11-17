package com.example.airaware.ui.home.airquality

data class PresetLocation(val name: String, val lat: Double, val lon: Double)

val presetLocations = listOf(
    PresetLocation("Delhi", 28.6139, 77.2090),
    PresetLocation("Mumbai", 19.0760, 72.8777),
    PresetLocation("Bangalore", 12.9716, 77.5946),
    PresetLocation("Kochi", 9.9312, 76.2673),
    PresetLocation("Chennai", 13.0827, 80.2707)
)
