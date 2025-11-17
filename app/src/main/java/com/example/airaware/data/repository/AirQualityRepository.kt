package com.example.airaware.data.repository

import com.example.airaware.data.remote.CountryResponse
import com.example.airaware.data.remote.NetworkModule

class AirQualityRepository {

    private val api = NetworkModule.airService

    suspend fun getCountryDetails(id: Int): CountryResponse {
        return api.getCountryDetails(id)
    }
}
