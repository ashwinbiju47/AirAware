package com.example.airaware.ui.home.airquality

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.airaware.data.local.HistoryItem
import com.example.airaware.data.local.UserDatabase
import com.example.airaware.data.remote.CountryResult
import com.example.airaware.data.repository.AirQualityRepository
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

    private val _countryData = MutableStateFlow<CountryResult?>(null)
    val countryData: StateFlow<CountryResult?> = _countryData

    private val _locationState = MutableStateFlow(LocationState())
    val locationState: StateFlow<LocationState> = _locationState

    fun loadCountry(id: Int) {
        viewModelScope.launch {
            try {
                val response = repo.getCountryDetails(id)
                _countryData.value = response.results.firstOrNull()
            } catch (e: Exception) {
                _countryData.value = null
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
        val currentCountry = countryData.value
        if (currentCountry != null) {
            viewModelScope.launch {
                val item = HistoryItem(
                    countryName = currentCountry.name,
                    timestamp = System.currentTimeMillis(),
                    details = "${currentCountry.parameters.size} parameters"
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
