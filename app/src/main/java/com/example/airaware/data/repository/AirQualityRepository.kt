package com.example.airaware.data.repository

import com.example.airaware.data.remote.*
import com.example.airaware.data.remote.NetworkModule
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class AirQualityRepository {

    private val api = NetworkModule.airService
    private val nominatimApi = NetworkModule.nominatimService
    private var historyDao: com.example.airaware.data.local.HistoryDao? = null

    fun setHistoryDao(dao: com.example.airaware.data.local.HistoryDao) {
        this.historyDao = dao
    }

    suspend fun getMeasurementData(countryId: Int): Pair<String, List<Measurement>> {
        return try {
            // 1. Get locations, sorted by ID (API default)
            val locationsResponse = api.getLocations(countryId = countryId, orderBy = "id")
            
            // 2. Find the first location that has data within the last 48 hours (approx check)
            // We sort locally by datetimeLast to get the most recent one
            val location = locationsResponse.results
                .filter { it.datetimeLast != null }
                .sortedByDescending { it.datetimeLast!!.utc }
                .firstOrNull()
            
            if (location != null) {
                // 3. Map sensor ID to Parameter details
                val sensorMap = location.sensors?.associateBy { it.id } ?: emptyMap()
                
                // 4. Get latest measurements for this location
                val latestResponse = api.getLocationLatest(location.id)
                
                val measurements = latestResponse.results.mapNotNull { result ->
                    val sensor = sensorMap[result.sensorsId]
                    if (sensor != null) {
                        Measurement(
                            parameter = sensor.parameter.name,
                            value = result.value,
                            unit = sensor.parameter.units
                        )
                    } else {
                        null
                    }
                }
                
                Pair(location.name, measurements)
            } else {
                Pair("No active station found", emptyList())
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Pair("Error: ${e.message}", emptyList())
        }
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
