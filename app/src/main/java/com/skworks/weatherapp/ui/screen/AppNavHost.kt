package com.skworks.weatherapp.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.skworks.weatherapp.ui.navigation.NavigationItem
import com.skworks.weatherapp.viewmodel.WeatherViewModel

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    viewModel: WeatherViewModel,
    navController: NavHostController,
    startDestination: String = NavigationItem.Search.route
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable(NavigationItem.Search.route) {
            WeatherScreen(navController, viewModel)
        }
        composable(NavigationItem.Details.route) {
            WeatherDetails(viewModel)
        }
    }
}