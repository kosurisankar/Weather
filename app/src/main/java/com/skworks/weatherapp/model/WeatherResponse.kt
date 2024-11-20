package com.skworks.weatherapp.model

/**
 * Data class representing the weather response from the OpenWeatherMap API.
 *
 * @property main Contains the main weather data
 * @property weather A list of weather conditions
 */
data class WeatherResponse(
    val main: Main,
    val weather: List<Weather>
)
/**
 * Data class representing the main weather details.
 *
 * @property temp The current temperature in degrees Celsius.
 * @property humidity The current humidity percentage.
 */
data class Main(
    val temp: Float,
    val humidity: Int
)
/**
 * Data class representing a specific weather condition.
 *
 * @property description A short description of the weather condition.
 * @property icon The icon code representing the weather condition.
 */
data class Weather(
    val description: String,
    val icon: String
)
/**
 * Data class representing the response for fetching city details based on coordinates.
 *
 * @property name The name of the city.
 */
data class CityResponse(
    val name: String
)
