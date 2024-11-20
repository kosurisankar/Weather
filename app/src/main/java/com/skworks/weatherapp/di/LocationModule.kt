package com.skworks.weatherapp.di

import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
/**
 * Dagger Module for providing location-related dependencies.
 *
 * This module supplies a `FusedLocationProviderClient` instance, which is used to access
 * location services such as obtaining the user's current location.
 */
@Module
@InstallIn(SingletonComponent::class)
object LocationModule {
    /**
     * Provides a singleton instance of `FusedLocationProviderClient`.
     *
     * The `FusedLocationProviderClient` is part of the Google Play services library and
     * is used for location-related tasks like retrieving the device's last known location
     * or requesting location updates.
     *
     * @param context The application context, injected using `@ApplicationContext`.
     * @return [FusedLocationProviderClient] instance to handle location services.
     */
    @Provides
    @Singleton
    fun provideFusedLocationProviderClient(
        @ApplicationContext context: Context
    ): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(context)
    }
}
