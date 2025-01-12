package com.skworks.weatherapp.ui.screen

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.skworks.weatherapp.ui.navigation.NavigationItem
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
fun WeatherScreen(navController: NavHostController, viewModel: WeatherViewModel) {
    val context: Context = LocalContext.current
    var showPermissionDialog by remember { mutableStateOf(false) }
    //the permission launcher
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                // Location permission granted
                viewModel.fetchLocation()
            } else {
                Toast.makeText(
                    context,
                    "Allow Permission to Auto fetch the location",
                    Toast.LENGTH_LONG
                ).show()
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
            Toast.makeText(context, "Allow Permission to Auto fetch location", Toast.LENGTH_LONG)
                .show()
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
        verticalArrangement = Arrangement.Center
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
            navController.navigate(NavigationItem.Details.route)
        }) {
            Text("Get Weather")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (viewModel.isLoading) {
            CircularProgressIndicator()
        }
    }
}

@Composable
fun WeatherDetails(viewModel: WeatherViewModel) {
    viewModel.weatherState?.let {
//    mockWeatherResponse.let {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                it.name,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                it.weather[0].description,
                style = MaterialTheme.typography.bodySmall
            )
            AsyncImage(
                model = "https://openweathermap.org/img/wn/${it.weather[0].icon}@4x.png",
                contentDescription = "Weather Icon",
                modifier = Modifier
                    .size(200.dp)
                    .padding(8.dp)
            )
            Text(
                "${it.main.temp}°C",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.padding(20.dp))
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .border(1.dp, Color.LightGray, shape = RoundedCornerShape(10.dp))
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.surface,
                                MaterialTheme.colorScheme.surfaceVariant
                            )
                        ), shape = RoundedCornerShape(10.dp)
                    ),
                horizontalArrangement = Arrangement.Center
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Humidity", style = MaterialTheme.typography.bodySmall)
                    Text(
                        "${it.main.humidity}%",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.padding(20.dp))
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Wind Speed", style = MaterialTheme.typography.bodySmall)
                    Text(
                        "${it.wind.speed} ml/h",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            viewModel.errorMessage?.let {
                Text("Error: $it", color = Color.Red)
            }
        }
    }
}

//val mockWeatherResponse = WeatherResponse(
//    main = Main(
//        temp = 25.5f,
//        humidity = 60
//    ),
//    weather = listOf(
//        Weather(
//            description = "Clear sky",
//            icon = "01d"
//        )
//    ),
//    wind = Wind(
//        speed = 5.5f
//    ),
//    name = "MockCity"
//)


