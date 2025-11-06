package github.detrig.weatherapp

import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput

class FindCityPage(private val composeTestRule: ComposeContentTestRule) {

    private val inputField = composeTestRule.onNodeWithTag("findCityInputField")
    private val foundCityUi = composeTestRule.onNodeWithTag("foundCityListUi", useUnmergedTree = true)

    fun input(text: String) {
        inputField.performTextInput(text)
    }

    fun assertCityFound(cityName: String, country: String) {
        composeTestRule.onNodeWithText("$cityName - $country", useUnmergedTree = true).assertExists()
    }

    fun clickFoundCity(cityName: String) {
        composeTestRule.onNodeWithTag(cityName).performClick()
    }
}