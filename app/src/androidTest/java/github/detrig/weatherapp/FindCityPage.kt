package github.detrig.weatherapp

import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput

class FindCityPage(private val composeTestRule: ComposeContentTestRule) {

    private val inputField = composeTestRule.onNodeWithTag("findCityInputField")
    private val foundCityUi = composeTestRule.onNodeWithTag("foundCityUi", useUnmergedTree = true)

    fun input(text: String) {
        inputField.performTextInput(text)
    }

    fun assertCityFound(cityName: String) {
        foundCityUi.assertTextEquals(cityName)
    }

    fun clickFoundCity(cityName: String) {
        composeTestRule.onNodeWithTag(cityName).performClick()
    }
}