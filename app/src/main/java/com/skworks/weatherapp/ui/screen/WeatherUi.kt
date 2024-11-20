package com.skworks.weatherapp.ui.screen

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.skworks.weatherapp.ui.components.PermissionRequest
import com.skworks.weatherapp.utils.PERMISSION_TEXT
import com.skworks.weatherapp.utils.PERMISSION_TITLE
import com.skworks.weatherapp.viewmodel.WeatherViewModel
/**
 * Composable function representing the WeatherScreen.
 *
 * This screen allows the user to:
 * - Search for weather data by entering a US city name.
 * - Fetch weather data for the user's current location (if location permission is granted).
 * - If permission is not granted fet the previously used city name
 * - Display weather information.
 *
 * @param navController Used for navigation between screens.
 */
@Composable
fun WeatherScreen(navController: NavHostController) {
    val viewModel: WeatherViewModel = hiltViewModel()
    val context : Context = LocalContext.current
    var showPermissionDialog by remember { mutableStateOf(false) }
    //the permission launcher
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                // Location permission granted
                viewModel.fetchLocation()
            }else {
                Toast.makeText(context,"Allow Permission to Auto fetch the location",Toast.LENGTH_LONG).show()
                showPermissionDialog = true
            }
        }
    )

    if (showPermissionDialog) {
        AlertDialog(
            onDismissRequest = { showPermissionDialog = false },
            title = { Text(PERMISSION_TITLE) },
            text = { Text(PERMISSION_TEXT) },
            confirmButton = {
                Button(onClick = {
                    showPermissionDialog = false
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", context.packageName, null)
                    }
                    context.startActivity(intent)
                }) {
                    Text("Open Settings")
                }
            },
            dismissButton = {
                Button(onClick = { showPermissionDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Calling permission request composable
    PermissionRequest(
        locationPermissionLauncher = locationPermissionLauncher,
        onPermissionGranted = {
            viewModel.fetchLocation()
        },
        onPermissionDenied = {
            Toast.makeText(context,"Allow Permission to Auto fetch location",Toast.LENGTH_LONG).show()
        }
    )

    // Observing the city name from ViewModel
    val cityName by viewModel.cityName.collectAsState()

    /**
     * Input field for the user to enter a city name.
     * - Updates the city name in the ViewModel whenever the user types.
     */
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        TextField(
            value = cityName,
            onValueChange = { newValue -> viewModel.updateCityName(newValue) },
            label = { Text("Enter US City") },
        )
        Spacer(modifier = Modifier.height(8.dp))
        /**
         * Button to fetch weather data for the entered city.
         * - Also saves the city name as the last searched city in DataStore.
         */
        Button(onClick = {
            viewModel.saveLastSearchedCity(cityName)
            viewModel.fetchWeather(cityName)
        }) {
            Text("Get Weather")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (viewModel.isLoading) {
            CircularProgressIndicator()
        } else {
            viewModel.weatherState?.let {
                Text("Temperature: ${it.main.temp}°C", style = MaterialTheme.typography.bodyLarge)
                Text("Humidity: ${it.main.humidity}%", style = MaterialTheme.typography.bodyLarge)
                Text(
                    "Condition: ${it.weather[0].description}",
                    style = MaterialTheme.typography.bodyLarge
                )
                AsyncImage(
                    model = "https://openweathermap.org/img/wn/${it.weather[0].icon}@2x.png",
                    contentDescription = "Weather Icon",
                    modifier = Modifier
                        .size(200.dp)
                        .padding(8.dp)
                )
            }
            viewModel.errorMessage?.let {
                Text("Error: $it", color = Color.Red)
            }
        }
    }
}
