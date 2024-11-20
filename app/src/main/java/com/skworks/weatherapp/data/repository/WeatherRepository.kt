package com.skworks.weatherapp.data.repository

import com.skworks.weatherapp.data.service.WeatherService
import com.skworks.weatherapp.model.CityResponse
import com.skworks.weatherapp.model.WeatherResponse
import com.skworks.weatherapp.utils.COUNTRY_CODE
import javax.inject.Inject
import javax.inject.Singleton
/**
 * Repository class responsible for interacting with the [WeatherService] to fetch weather and city data.
 *
 * This class abstracts the API calls to the OpenWeatherMap service, allowing the ViewModel or other
 * components to interact with it in a simplified manner.
 */
@Singleton
class WeatherRepository @Inject constructor(private val service: WeatherService) {
    /**
     * Fetches weather information for a given city.
     *
     * @param city The name of the city to fetch weather data for, with a country code appended.
     * @param apiKey The API key to authenticate the request.
     * @return A [WeatherResponse] object containing weather details for the specified city.
     */
    suspend fun fetchWeather(city: String, apiKey: String): WeatherResponse {
        return service.getWeatherByCity(city + COUNTRY_CODE, apiKey)
    }
    /**
     * Fetches city data based on geographic coordinates (latitude and longitude).
     *
     * @param latitude The latitude of the location to fetch data for.
     * @param longitude The longitude of the location to fetch data for.
     * @param apiKey The API key for authentication.
     * @return A list of [CityResponse] objects containing city information based on the coordinates.
     */
    suspend fun fetchCityByCoordinates(
        latitude: Double,
        longitude: Double,
        apiKey: String
    ): List<CityResponse> {
        return service.getCityByCoordinates(latitude, longitude, apiKey = apiKey)
    }
}