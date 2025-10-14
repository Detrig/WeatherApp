package github.detrig.weatherapp

import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onNodeWithTag

class WeatherPage(composeTestRule: ComposeContentTestRule) {

    private val cityNameNode = composeTestRule.onNodeWithTag("City")
    private val weatherTemperatureNode = composeTestRule.onNodeWithTag("Weather")
    private val feelTempNode = composeTestRule.onNodeWithTag("FeelTemp")
    private val windSpeedNode = composeTestRule.onNodeWithTag("WindSpeed")

    fun assertCityName(cityName: String) {
        cityNameNode.assertTextEquals(cityName)
    }

    fun assertWeatherDisplayed(temp: String, feelTemp: String, windSpeed: String) {
        weatherTemperatureNode.assertTextEquals(temp)
        feelTempNode.assertTextEquals(feelTemp)
        windSpeedNode.assertTextEquals(windSpeed)
    }

}
