package com.skworks.weatherapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
/**
 * The Application class for the Weather app.
 *
 * This class is annotated with `@HiltAndroidApp` to trigger Hilt's code generation.
 * Hilt is used for Dependency Injection (DI), enabling the automatic injection of
 * dependencies throughout the app.
 */
@HiltAndroidApp
class WeatherApp : Application()
