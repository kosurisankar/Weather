package com.skworks.weatherapp.ui.navigation

enum class Screen {
    SEARCH,
    DETAILS,
}
sealed class NavigationItem(val route: String) {
    data object Search : NavigationItem(Screen.SEARCH.name)
    data object Details : NavigationItem(Screen.DETAILS.name)
}


