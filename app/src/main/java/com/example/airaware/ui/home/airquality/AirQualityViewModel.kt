package com.example.airaware.ui.home.airquality

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.airaware.data.remote.CountryResult
import com.example.airaware.data.repository.AirQualityRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.airaware.BuildConfig

class AirQualityViewModel(
    private val repo: AirQualityRepository = AirQualityRepository()
) : ViewModel() {

    private val _countryData = MutableStateFlow<CountryResult?>(null)
    val countryData: StateFlow<CountryResult?> = _countryData

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

}
