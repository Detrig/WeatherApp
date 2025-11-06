package github.detrig.weatherapp

import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onNodeWithTag

class WeatherPage(private val composeTestRule: ComposeContentTestRule) {

    private val cityNameNode = composeTestRule.onNodeWithTag("City")
    private val weatherTemperatureNode = composeTestRule.onNodeWithTag("Weather")
    private val feelTempNode = composeTestRule.onNodeWithTag("FeelTemp")
    private val windSpeedNode = composeTestRule.onNodeWithTag("WindSpeed")
    private val uvNode = composeTestRule.onNodeWithTag("Uv")
    private val backgroundNode = composeTestRule.onNodeWithTag("WeatherBackground")

    fun assertCityName(cityName: String) {
        cityNameNode.assertTextEquals(cityName)
    }

    fun assertWeatherDisplayed(temp: String, feelTemp: String, windSpeed: String, uv: String) {
        weatherTemperatureNode.assertTextEquals(temp)
        feelTempNode.assertTextEquals(feelTemp)
        windSpeedNode.assertTextEquals(windSpeed)
        uvNode.assertTextEquals(uv)
    }

    fun assertBackgroundForCondition(expectedConditionTag: String) {
        composeTestRule.onNodeWithTag(expectedConditionTag).assertExists()
    }
}
