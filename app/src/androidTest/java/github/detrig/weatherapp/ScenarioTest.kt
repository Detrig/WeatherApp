package github.detrig.weatherapp

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidTest
import github.detrig.weatherapp.core.RunAsync
import github.detrig.weatherapp.findcity.domain.FindCityRepository
import github.detrig.weatherapp.findcity.domain.FoundCity
import github.detrig.weatherapp.findcity.presentation.FindCityScreen
import github.detrig.weatherapp.findcity.presentation.FindCityScreenUi
import github.detrig.weatherapp.findcity.presentation.FindCityViewModel
import github.detrig.weatherapp.findcity.presentation.FoundCityUi
import github.detrig.weatherapp.weather.domain.WeatherInCity
import github.detrig.weatherapp.weather.domain.WeatherRepository
import github.detrig.weatherapp.weather.presentation.WeatherScreen
import github.detrig.weatherapp.weather.presentation.WeatherScreenUi
import github.detrig.weatherapp.weather.presentation.WeatherViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Rule

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class ScenarioTest {

    @get:Rule
    val composeTestRule = createComposeRule()   //чтобы могли создавать composable в ui тестах

    @Test
    fun findCityAndShowWeather(): Unit = with(composeTestRule) {
        val findCityMapper: FindCityResult.Mapper<FoundCityUi> = FindCityUiMapper()
        val findCityViewModel = FindCityViewModel(
            mapper = findCityMapper,
            repository = FakeFindCityRepository(),
            savedStateHandle = SavedStateHandle(),
            runAsync = FakeRunAsync()
        )

        val weatherMapper : WeatherResult.Mapper<WeatherScreenUi> = WeatherUiMapper()
        val weatherViewModel = WeatherViewModel(
            mapper = weatherMapper,
            repository = FakeWeatherRepository(),
            savedStateHandle = SavedStateHandle(),
            runAsync = FakeRunAsync()
        )

        setContent {
            val navController: NavHostController = rememberNavController()
            NavHost(navController = navController, startDestination = "findCityScreen") {
                composable("findCityScreen") {
                    FindCityScreen(
                        viewModel = findCityViewModel,
                        navigateToWeatherScreen = {
                            navController.navigate("weatherScreen")
                        }
                    )
                }

                composable("weatherScreen") {
                    WeatherScreen(
                        viewModel = weatherViewModel
                    )
                }
            }
        }

        startUiTest()
    }

    @Test
    fun findCityAndShowWeatherUi(): Unit = with(composeTestRule) {
        setContent {
            val navController: NavHostController = rememberNavController()
            NavHost(navController = navController, startDestination = "findCityScreen") {
                composable("findCityScreen") {
                    val input = rememberSaveable { mutableStateOf("") }
                    val shouldShowNoConnectionError = rememberSaveable { mutableStateOf(true) }

                    FindCityScreenUi(
                        input = input.value,
                        onInputChange = { text: String ->
                            input.value = text
                        },
                        foundCityUi = if (input.value.isEmpty())
                            FoundCityUi.Empty
                        else if (input.value == "FUCK")
                            if (shouldShowNoConnectionError.value) {
                                FoundCityUi.NoConnectionError
                            } else {
                                FoundCityUi.Empty
                            }
                        else
                            FoundCityUi.Base(
                                listOf(
                                    FoundCity(
                                        name = "Moscow",
                                        country = "Russia",
                                        latitude = 55.75,
                                        longitude = 37.61
                                    ),
                                    FoundCity(
                                        name = "Moscow",
                                        country = "USA",
                                        latitude = 55.75,
                                        longitude = 37.61
                                    )
                                )
                            ),
                        onFoundCityClick = { foundCity: FoundCity ->
                            navController.navigate("weatherScreen")
                        },
                        onRetryClick = {
                            shouldShowNoConnectionError.value = false
                        }
                    )
                }

                composable("weatherScreen") {
                    val shouldShowError = rememberSaveable { mutableStateOf(true) }

                    if (shouldShowError.value) {
                        shouldShowError.value = false
                        WeatherScreenUi.NoConnectionError.Show(onRetryClick = {
                            shouldShowError.value = false
                        })
                    } else {
                        WeatherScreenUi.Base(
                            cityParams = WeatherInCity(
                                cityName = "Moscow",
                                temperature = 33.1f,
                                feelTemperature = 31.2f,
                                windSpeed = 5.5f,
                                uv = 0.4f,
                                condition = "Sunny"
                            )
                        ).Show(onRetryClick = {
                            //nothing to do
                        })
                    }
                }

            }
        }
        startUiTest()
    }

    private fun startUiTest() {
        val findCityPage = FindCityPage(composeTestRule = composeTestRule)

        findCityPage.input(text = "FUCK")
        findCityPage.assertNoConnectionIsDisplayed()

        findCityPage.clickRetry()
        findCityPage.assertEmptyResult()

        findCityPage.input(text = "Mos")
        findCityPage.assertCityFound(cityName = "Moscow", country = "Russia")
        findCityPage.assertCityFound(cityName = "Moscow", country = "USA")

        findCityPage.clickFoundCity(cityName = "Moscow - Russia")
        val weatherPage = WeatherPage(composeTestRule = composeTestRule)
        weatherPage.assertNoConnectionIsDisplayed()

        weatherPage.clickRetry()
        weatherPage.assertCityName(cityName = "Moscow")
        weatherPage.assertWeatherDisplayed(
            temp = "33.1",
            feelTemp = "31.2",
            windSpeed = "5.5",
            uv = "0.4"
        )
        weatherPage.assertBackgroundForCondition("SunnyBackground")
    }
}


class FakeFindCityRepository : FindCityRepository {

    private var shouldShowError = true

    override suspend fun findCity(query: String): FindCityResult {
        if (query.trim().isEmpty())
            throw IllegalStateException("repository should not accept empty query")

        if (query == "FUCK") {
            shouldShowError = false
            return FindCityResult.Failed(error = NoInternetException)
        } else {
            return FindCityResult.Empty
        }

        if (query == "Mos")
            return FindCityResult.Base(
                listOf(
                    FoundCity(
                        name = "Moscow",
                        latitude = 55.75,
                        country = "Russia",
                        longitude = 37.61
                    ),
                    FoundCity(
                        name = "Moscow",
                        country = "USA",
                        latitude = 55.75,
                        longitude = 37.61
                    )
                )
            )

        throw IllegalStateException("not supported for this test")
    }

    override suspend fun saveCity(foundCity: FoundCity) {
        if (foundCity != FoundCity(
                name = "Moscow",
                country = "Russia",
                latitude = 55.75,
                longitude = 37.61
            )
        )
            throw IllegalStateException("save called with wrong argument $foundCity")
    }
}

private class FakeWeatherRepository : WeatherRepository {

    private var shouldShowError = true

    override suspend fun weather(): WeatherResult {
        if (shouldShowError) {
            shouldShowError = false
            return WeatherResult.Failed(error = NoInternetException)
        } else {
            return WeatherResult.Base(
                WeatherInCity(
                    cityName = "Moscow",
                    temperature = 33.1f,
                    feelTemperature = 31.2f,
                    windSpeed = 5.5f,
                    uv = 0.4f,
                    condition = "Sunny"
                )
            )
        }
    }
}

class FakeRunAsync : RunAsync {

    private lateinit var resultCached: Any
    private lateinit var uiCached: (Any) -> Unit

    override fun <T : Any> runAsync(
        scope: CoroutineScope,
        background: suspend () -> T,
        ui: (T) -> Unit
    ) {
        runBlocking {
            val result = background()
            ui(result)
        }
    }

    fun returnResult() {
        uiCached.invoke(resultCached)
    }
}