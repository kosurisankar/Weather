package com.skworks.weatherapp.di

import android.content.Context
import com.skworks.weatherapp.data.datastore.DataStoreHelper
import com.skworks.weatherapp.data.service.WeatherService
import com.skworks.weatherapp.utils.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
/**
 * Dagger Module for providing application-level dependencies.
 *
 * This module is responsible for providing instances of services and utilities like Retrofit,
 * WeatherService, and DataStoreHelper. These instances are scoped to the SingletonComponent,
 * meaning they will persist for the lifetime of the application.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    /**
     * Provides an instance of DataStoreHelper.
     *
     * @param context The application context, injected using @ApplicationContext.
     * @return [DataStoreHelper] instance for managing persistent data.
     */
    @Provides
    @Singleton
    fun provideDataStoreHelper(@ApplicationContext context: Context): DataStoreHelper {
        return DataStoreHelper(context)
    }
    /**
     * Provides a configured instance of Retrofit.
     *
     * Retrofit is used to handle network communication. This instance is configured
     * with a base URL and a Gson converter for handling JSON responses.
     *
     * @return [Retrofit] instance for making network API calls.
     */
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    /**
     * Provides an instance of WeatherService created by Retrofit.
     *
     * The WeatherService defines the API endpoints used to fetch weather data.
     * Retrofit generates the implementation of this interface.
     *
     * @param retrofit The Retrofit instance used to create the service.
     * @return [WeatherService] instance for accessing weather API endpoints.
     */
    @Provides
    @Singleton
    fun provideWeatherService(retrofit: Retrofit): WeatherService {
        return retrofit.create(WeatherService::class.java)
    }
}