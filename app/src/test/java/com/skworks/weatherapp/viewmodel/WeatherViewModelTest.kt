package com.skworks.weatherapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.skworks.weatherapp.data.datastore.DataStoreHelper
import com.skworks.weatherapp.model.CityResponse
import com.skworks.weatherapp.model.WeatherResponse
import com.skworks.weatherapp.data.repository.WeatherRepository
import com.skworks.weatherapp.utils.API_KEY
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

class WeatherViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: WeatherViewModel

    private lateinit var weatherViewModel: WeatherViewModel
    private val weatherRepository: WeatherRepository = mock()
    private val dataStoreHelper: DataStoreHelper = mock()

    private val cityResponse = CityResponse(
        name = "Houston"
    )

    @Before
    fun setUp() {
        // Initialize ViewModel with mock dependencies
        weatherViewModel = WeatherViewModel(
            dataStoreHelper = dataStoreHelper,
            repository = weatherRepository,
            fusedLocationProviderClient = mock()
        )
    }

    @Test
    fun testGetCityNameUpdatesCityData() = runBlockingTest {
        // Mock the response from the repository
        `when`(weatherRepository.fetchCityByCoordinates(40.7128, -74.0060, API_KEY))
            .thenReturn(listOf(cityResponse))

        // Observer to capture LiveData updates
        val cityObserver = mock<Observer<CityResponse>>()

        // Observe the LiveData
//        weatherViewModel.cityNameByLocation.observeForever(cityObserver)

        // Call the function that should update the LiveData
        weatherViewModel.getCityName(40.7128, -74.0060)

        // Verify the LiveData has been updated
        verify(cityObserver).onChanged(cityResponse)

        // Assert that the city name was updated correctly in the state
        assertEquals("Houston", weatherViewModel.cityName.value)
    }

    @Test
    fun testFetchWeatherUpdatesWeatherState() = runBlockingTest {
        // Mock the weather fetch response
        val weatherResponse = mock<WeatherResponse>()
        `when`(weatherRepository.fetchWeather("Houston", API_KEY))
            .thenReturn(weatherResponse)

        // Call fetchWeather
        weatherViewModel.fetchWeather("Houston")

        // Verify that the weather state is updated
        assertEquals(weatherResponse, weatherViewModel.weatherState)
    }
}
