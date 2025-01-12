package com.skworks.weatherapp.data.service

import com.skworks.weatherapp.model.CityResponse
import com.skworks.weatherapp.model.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query
/**
 * Retrofit interface for interacting with the OpenWeatherMap API.
 *
 * This interface defines the API endpoints that are used to fetch weather data by city name
 * and retrieve city information based on geographic coordinates (latitude and longitude).
 */
interface WeatherService {
    /**
     * Fetches weather data for a given city name.
     *
     * @param city The name of the city to fetch weather data for.
     * @param apiKey The API key to authenticate the request.
     * @return A [WeatherResponse] object containing weather details for the specified city.
     */
    @GET("data/2.5/weather")
    suspend fun getWeatherByCity(
        @Query("q") city: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric"
    ): WeatherResponse

    /**
     * Retrieves city data by geographic coordinates (latitude and longitude).
     *
     * @param latitude The latitude of the location.
     * @param longitude The longitude of the location.
     * @param limit The number of results to return. Default is 1.
     * @param apiKey The API key for authentication.
     * @return A list of [CityResponse] objects containing city information.
     */
    @GET("geo/1.0/reverse")
    suspend fun getCityByCoordinates(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("limit") limit: Int = 1,
        @Query("appid") apiKey: String
    ): List<CityResponse>
}