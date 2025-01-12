package com.skworks.weatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.skworks.weatherapp.ui.screen.AppNavHost
import com.skworks.weatherapp.viewmodel.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * The main entry point for the Android application.
 *
 * This activity is annotated with `@AndroidEntryPoint` to integrate with Hilt
 * for dependency injection. It uses Jetpack Compose for UI and a `NavHost`
 * to manage navigation between screens.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    /**
     * Called when the activity is first created.
     *
     * Sets up the Compose content and initializes the navigation host to manage the
     * navigation flow in the application.
     *
     * @param savedInstanceState The previously saved instance state, if available.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: WeatherViewModel = hiltViewModel()
            Surface {
                AppNavHost(viewModel = viewModel, navController = rememberNavController())
            }
        }
    }
}