package com.example.airaware.ui.home.airquality

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.airaware.data.local.HistoryItem
import com.example.airaware.data.local.UserDatabase
import com.example.airaware.data.remote.Measurement
import com.example.airaware.data.repository.AirQualityRepository
import com.example.airaware.utils.AqiUtils
import com.example.airaware.utils.AqiResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.airaware.BuildConfig

class AirQualityViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repo: AirQualityRepository = AirQualityRepository()
    
    init {
        val db = UserDatabase.getDatabase(application)
        repo.setHistoryDao(db.historyDao())
    }

    val historyList = repo.getHistory()



    private val _measurements = MutableStateFlow<List<Measurement>>(emptyList())
    val measurements: StateFlow<List<Measurement>> = _measurements

    private val _aqiResult = MutableStateFlow<AqiResult?>(null)
    val aqiResult: StateFlow<AqiResult?> = _aqiResult

    private val _stationName = MutableStateFlow<String?>(null)
    val stationName: StateFlow<String?> = _stationName

    private val _locationState = MutableStateFlow(LocationState())
    val locationState: StateFlow<LocationState> = _locationState

    fun loadCountry(id: Int) {
        viewModelScope.launch {
            try {
                _measurements.value = emptyList()
                _stationName.value = "Loading..."
                
                val (foundStationName, foundMeasurements) = repo.getMeasurementData(id)
                _stationName.value = foundStationName
                _measurements.value = foundMeasurements
                
                // Calculate AQI
                val pm25 = foundMeasurements.find { it.parameter == "pm25" }?.value
                val pm10 = foundMeasurements.find { it.parameter == "pm10" }?.value
                _aqiResult.value = AqiUtils.calculateAQI(pm25, pm10)
                
                // We no longer populate _countryData from the API as it was removed.
                // Depending on UI needs we might need to fake it or remove it.
                // For now, let's leave it null or just not use it.
            } catch (e: Exception) {
                _stationName.value = "Error fetching data"
                _measurements.value = emptyList()
            }
        }
    }

    fun detectLocation(context: android.content.Context) {
        viewModelScope.launch {
            _locationState.value = _locationState.value.copy(status = LocationStatus.DetectingLocation)
            val locationManager = com.example.airaware.utils.LocationManager(context)
            val location = locationManager.getCurrentLocation()

            if (location != null) {
                val countryName = repo.reverseGeocode(location.latitude, location.longitude)
                if (countryName != null) {
                    val matchedCountry = presetCountries.find { 
                        it.name.equals(countryName, ignoreCase = true) || 
                        countryName.contains(it.name, ignoreCase = true) 
                    }

                    if (matchedCountry != null) {
                        _locationState.value = LocationState(
                            lat = location.latitude,
                            lon = location.longitude,
                            country = matchedCountry.name,
                            status = LocationStatus.LocationDetected
                        )
                        loadCountry(matchedCountry.id)
                    } else {
                        _locationState.value = _locationState.value.copy(status = LocationStatus.Error)
                    }
                } else {
                    _locationState.value = _locationState.value.copy(status = LocationStatus.Error)
                }
            } else {
                _locationState.value = _locationState.value.copy(status = LocationStatus.Error)
            }
        }
    }

    fun setPermissionDenied() {
        _locationState.value = _locationState.value.copy(status = LocationStatus.PermissionDenied)
    }

    fun saveCurrentState() {
        val currentStation = stationName.value
        val currentMeasurements = measurements.value
        if (currentStation != null && currentMeasurements.isNotEmpty()) {
            viewModelScope.launch {
                val item = HistoryItem(
                    countryName = currentStation,
                    timestamp = System.currentTimeMillis(),
                    details = "${currentMeasurements.size} parameters"
                )
                repo.saveHistory(item)
            }
        }
    }
}

data class LocationState(
    val lat: Double? = null,
    val lon: Double? = null,
    val country: String? = null,
    val status: LocationStatus = LocationStatus.Idle
)

enum class LocationStatus {
    Idle,
    DetectingLocation,
    LocationDetected,
    PermissionDenied,
    Error
}
