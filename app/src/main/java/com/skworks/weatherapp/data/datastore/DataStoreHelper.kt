package com.skworks.weatherapp.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Extension property to initialize a DataStore instance for storing preferences.
 *
 * This uses the `preferencesDataStore` delegate to create a singleton DataStore
 * instance for the application, named "weather_preferences".
 */
val Context.dataStore by preferencesDataStore(name = "weather_preferences")
/**
 * A helper class to manage DataStore operations for storing and retrieving user preferences.
 *
 * @param context The application context required to access the DataStore instance.
 */
class DataStoreHelper(private val context: Context) {

    private val LAST_SEARCHED_CITY_KEY = stringPreferencesKey("last_searched_city")
    /**
     * A Flow that retrieves the last searched city from DataStore.
     *
     * @return [Flow] emitting the last searched city or null if no value is stored.
     */
    val lastSearchedCityFlow: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[LAST_SEARCHED_CITY_KEY]
        }
    /**
     * Saves the last searched city to the DataStore.
     *
     * This function runs in a coroutine, updating the value associated with
     * the `LAST_SEARCHED_CITY_KEY`.
     *
     * @param city The name of the city to be stored as the last searched city.
     */
    suspend fun saveLastSearchedCity(city: String) {
        context.dataStore.edit { preferences ->
            preferences[LAST_SEARCHED_CITY_KEY] = city
        }
    }
}
