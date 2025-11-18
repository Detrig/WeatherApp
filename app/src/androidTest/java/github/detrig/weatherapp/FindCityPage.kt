package github.detrig.weatherapp

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTextReplacement

class FindCityPage(private val composeTestRule: ComposeContentTestRule) {

    private val noConnectionError = composeTestRule.onNodeWithTag("noInternetConnectionLayer")
    private val retryButton = composeTestRule.onNodeWithTag("retryButton")
    private val inputField = composeTestRule.onNodeWithTag("findCityInputField")
    private val foundCityUi = composeTestRule.onNodeWithTag("foundCityListUi", useUnmergedTree = true)

    fun input(text: String) {
        inputField.performTextReplacement(text)
    }

    fun assertCityFound(cityName: String, country: String) {
        composeTestRule.onNodeWithText("$cityName - $country", useUnmergedTree = true).assertExists()
    }

    fun clickFoundCity(cityName: String) {
        composeTestRule.onNodeWithText(cityName).performClick()
    }

    fun assertNoConnectionIsDisplayed() {
        noConnectionError.assertIsDisplayed()
    }

    fun clickRetry() {
        retryButton.performClick()
    }

    fun assertEmptyResult() {
        noConnectionError.assertDoesNotExist()
        retryButton.assertDoesNotExist()
        foundCityUi.assertDoesNotExist()
    }
}