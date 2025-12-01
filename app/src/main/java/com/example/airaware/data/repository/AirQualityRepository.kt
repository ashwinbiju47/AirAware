package com.example.airaware.data.repository

import com.example.airaware.data.remote.CountryResponse
import com.example.airaware.data.remote.NetworkModule

class AirQualityRepository {

    private val api = NetworkModule.airService
    private val nominatimApi = NetworkModule.nominatimService
    private var historyDao: com.example.airaware.data.local.HistoryDao? = null

    fun setHistoryDao(dao: com.example.airaware.data.local.HistoryDao) {
        this.historyDao = dao
    }

    suspend fun getCountryDetails(id: Int): CountryResponse {
        return api.getCountryDetails(id)
    }

    suspend fun reverseGeocode(lat: Double, lon: Double): String? {
        return try {
            val response = nominatimApi.reverseGeocode(lat, lon)
            response.address?.country
        } catch (e: Exception) {
            null
        }
    }

    suspend fun saveHistory(item: com.example.airaware.data.local.HistoryItem) {
        historyDao?.insert(item)
    }

    fun getHistory(): kotlinx.coroutines.flow.Flow<List<com.example.airaware.data.local.HistoryItem>>? {
        return historyDao?.getAllHistory()
    }
}
