package com.skworks.weatherapp.UI

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.compose.rememberNavController
import com.skworks.weatherapp.model.Main
import com.skworks.weatherapp.model.Weather
import com.skworks.weatherapp.model.WeatherResponse
import com.skworks.weatherapp.model.Wind
import com.skworks.weatherapp.ui.screen.WeatherScreen
import com.skworks.weatherapp.viewmodel.WeatherViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

class WeatherScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Mock
    lateinit var mockViewModel: WeatherViewModel

    private lateinit var cityNameFlow: StateFlow<String>

    @Before
    fun setup() {
        // Mock ViewModel and its flow data
        cityNameFlow = MutableStateFlow("Houston")
        whenever(mockViewModel.cityName).thenReturn(cityNameFlow)
        // Setup the Compose UI for testing
        composeTestRule.setContent {
            WeatherScreen(navController = rememberNavController(), mockViewModel)
        }
    }

    @Test
    fun testCityNameTextField() {
        // Arrange
        val cityNameFlow = MutableStateFlow("Houston")
        val mockViewModel = mock(WeatherViewModel::class.java)

        // Make sure the ViewModel returns the flow with "Houston"
        whenever(mockViewModel.cityName).thenReturn(cityNameFlow)

        // Set the content with mock ViewModel
        composeTestRule.setContent {
            WeatherScreen(navController = rememberNavController(), mockViewModel)
        }
        // Test that the TextField correctly displays the city name from the ViewModel
        composeTestRule.onNodeWithText("Houston").assertExists()
    }

    @Test
    fun testWeatherInfoDisplay() {
        // Mock the weather data and test that the weather information gets displayed
        whenever(mockViewModel.weatherState).thenReturn(
            WeatherResponse(
                main = Main(temp = 25.0F, humidity = 60),
                weather = listOf(Weather(description = "Clear", icon = "01d")),
                name = "Houston",
                wind = Wind(speed = 22.3F)
            )
        )
        // Perform UI interaction or assertions
        composeTestRule.onNodeWithText("Temperature: 25.0°C").assertExists()
        composeTestRule.onNodeWithText("Humidity: 60%").assertExists()
        composeTestRule.onNodeWithText("Condition: Clear").assertExists()
    }

    @Test
    fun testLoadingState() {
        // Simulate the loading state and verify the loading indicator is visible
        whenever(mockViewModel.isLoading).thenReturn(true)
        composeTestRule.onNodeWithTag("loadingIndicator").assertIsDisplayed()

        // Simulate the loaded state
        whenever(mockViewModel.isLoading).thenReturn(false)
        composeTestRule.onNodeWithTag("loadingIndicator").assertDoesNotExist()
    }
}