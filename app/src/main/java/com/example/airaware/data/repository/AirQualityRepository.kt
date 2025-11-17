package com.example.airaware.data.repository

import com.example.airaware.data.remote.NetworkModule

class AirQualityRepository {

    private val api = NetworkModule.airService

    suspend fun getAQI(lat: Double, lon: Double) = api.getAirQuality("$lat,$lon")
}
