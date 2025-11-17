package com.example.airaware.ui.home.airquality

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.airaware.data.repository.AirQualityRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AirQualityViewModel(
    private val repo: AirQualityRepository = AirQualityRepository()
) : ViewModel() {

    private val _aqi = MutableStateFlow<Double?>(null)
    val aqi: StateFlow<Double?> get() = _aqi

    fun loadAQI(lat: Double, lon: Double) {
        viewModelScope.launch {
            try {
                val data = repo.getAQI(lat, lon)
                val first = data.results.firstOrNull()
                val pm25 = first?.measurements?.find { it.parameter == "pm25" }
                _aqi.value = pm25?.value
            } catch (e: Exception) {
                _aqi.value = null
            }
        }
    }
}
