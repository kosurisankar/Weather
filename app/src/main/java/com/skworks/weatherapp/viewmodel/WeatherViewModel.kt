package com.skworks.weatherapp.viewmodel

import android.annotation.SuppressLint
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.skworks.weatherapp.data.datastore.DataStoreHelper
import com.skworks.weatherapp.model.WeatherResponse
import com.skworks.weatherapp.data.repository.WeatherRepository
import com.skworks.weatherapp.utils.API_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
// WeatherViewModel
@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val dataStoreHelper: DataStoreHelper,
    private val repository: WeatherRepository,
    private val fusedLocationProviderClient: FusedLocationProviderClient
) : ViewModel() {

    private val _cityName = MutableStateFlow("")
    var cityName: StateFlow<String> = _cityName

    // last searched city to the UI
    private val _lastSearchedCity = MutableStateFlow<String?>("")

    fun updateCityName(newCityName: String) {
        _cityName.value = newCityName
    }

    init {
        // Collects the last searched city from DataStore and updates the state flow.
        viewModelScope.launch {
            dataStoreHelper.lastSearchedCityFlow.collect { city ->
                _lastSearchedCity.value = city
                city?.let {
                    updateCityName(it)
                }
            }
        }
    }

    //Method to fetch location and update city name based on coordinates
    @SuppressLint("MissingPermission")
    fun fetchLocation() {
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            location?.let {
                getCityName(it.latitude, it.longitude)
            }
        }
    }
    /**
     * Saves the provided city name in DataStore for future retrieval.
     */
    fun saveLastSearchedCity(city: String) {
        viewModelScope.launch {
            dataStoreHelper.saveLastSearchedCity(city)
        }
    }
    // State variables for managing UI state during API calls.
    var weatherState by mutableStateOf<WeatherResponse?>(null)
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)
    /**
     * Fetches weather data for the specified city name.
     * Updates [weatherState] or [errorMessage] based on the result.
     */
    fun fetchWeather(city: String) {
        viewModelScope.launch {
            isLoading = true
            try {
                weatherState = repository.fetchWeather(city, API_KEY)
                errorMessage = null
            } catch (e: Exception) {
                errorMessage = "Error fetching weather data"
            }
            isLoading = false
        }
    }
    /**
     * Fetches city name based on latitude and longitude.
     * Updates [_cityName] with the fetched city data.
     */
    fun getCityName(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = repository.fetchCityByCoordinates(latitude, longitude, API_KEY)
                if (response.isNotEmpty()) {
                    updateCityName(response.first().name)
                }
            } catch (e: Exception) {
                errorMessage = "Error fetching weather data"
            }
            isLoading = false
        }
    }
}
