package github.detrig.weatherapp

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Rule

@RunWith(AndroidJUnit4::class)
class ScenarioTest {

    @get:Rule
    val composeTestRule = createComposeRule()   //чтобы могли создавать composable в ui тестах

    @Test
    fun findCityAndShowWeather(): Unit = with(composeTestRule) {
        setContent {
            val navController: NavHostController = rememberNavController()
            NavHost(navController = navController, startDestination = "findCityScreen") {
                composable("findCityScreen") {
                    FindCityScreen(
                        viewModel = FindCityViewModel(
                            savedStateHandle = SavedStateHandle(),
                            repository = FakeFindCityRepository()
                        ),
                        navigateToWeatherScreen = {
                            navController.navigate("weatherScreen")
                        }
                    ) {
                        navController.navigate("second")
                    }
                }
                composable("weatherScreen") {
                    WeatherScreen(
                        viewModel = WeatherViewModel(
                            savedStateHandle = SavedStateHandle(),
                            repository = FakeWeatherRepository()
                        )
                    )
                }
            }
        }
    }

    val findCityPage = FindCityPage(composeTestRule = composeTestRule)

    findCityPage.input(text = "Mos")
    findCityPage.assertCityFound(cityName = "Moscow")

    findCityPage.clickFoundCity(cityName = "Moscow")
    val weatherPage = WeatherPage(composeTestRule = composeTestRule)
    weatherPage.assertCityName(cityName = "Moscow city")
    weatherPage.assertWeatherDisplayed(temp = "33", feelTemp = "31", windSpeed = "5.5")
}

private class FakeFindCityRepository : FindCityRepository {

    override suspend fun findCity(query: String): FoundCity {
        if (query == "Mos")
            return FoundCity(
                name = "Moscow",
                latitude = 55.75,
                longitude = 37.61
            )
        throw IllegalStateException("not supported for this test")
    }

    override suspend fun saveCity(foundCity: FoundCity) {
        if (foundCity != FoundCity(name = "Moscow", latitude = 55.75, longitude = 37.61))
            throw IllegalStateException("save called with wrong argument $foundCity")
    }
}

private class FakeWeatherRepository : WeatherRepository {

    override suspend fun weather() : WeatherForCity {
        return WeatherInCity(cityName = "Moscow city", temperature = "33", feelTemperature = "31", windSpped = "5.5")
    }
}