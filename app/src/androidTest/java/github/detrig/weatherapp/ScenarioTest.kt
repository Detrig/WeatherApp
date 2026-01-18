package github.detrig.weatherapp

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.Provides
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import github.detrig.weatherapp.core.RunAsync
import github.detrig.weatherapp.findcity.domain.FindCityRepository
import github.detrig.weatherapp.findcity.domain.FindCityResult
import github.detrig.weatherapp.findcity.domain.models.FoundCity
import github.detrig.weatherapp.core.NoInternetException
import github.detrig.weatherapp.core.network.NetworkStatusRepository
import github.detrig.weatherapp.core.notification.NotificationsPrefsImpl
import github.detrig.weatherapp.findcity.presentation.FindCityScreen
import github.detrig.weatherapp.findcity.presentation.FindCityScreenUi
import github.detrig.weatherapp.findcity.presentation.mappers.FindCityUiMapper
import github.detrig.weatherapp.findcity.presentation.FindCityViewModel
import github.detrig.weatherapp.findcity.presentation.FoundCityScreenUiState
import github.detrig.weatherapp.settings.SettingsScreen
import github.detrig.weatherapp.settings.SettingsViewModel
import github.detrig.weatherapp.weather.domain.models.Weather
import github.detrig.weatherapp.weather.domain.WeatherRepository
import github.detrig.weatherapp.weather.domain.WeatherResult
import github.detrig.weatherapp.weather.domain.models.AirQuality
import github.detrig.weatherapp.weather.domain.notification.NotificationsPrefs
import github.detrig.weatherapp.weather.domain.schedule.WeatherUpdateScheduler
import github.detrig.weatherapp.weather.domain.widget.WeatherWidgetUpdater
import github.detrig.weatherapp.weather.presentation.WeatherScreen
import github.detrig.weatherapp.weather.presentation.WeatherScreenUiState
import github.detrig.weatherapp.weather.presentation.mappers.WeatherUiMapper
import github.detrig.weatherapp.weather.presentation.WeatherViewModel
import github.detrig.weatherapp.weather.presentation.mappers.AirQualityUiMapper
import github.detrig.weatherapp.weather.presentation.mappers.ForecastDayUiMapper
import github.detrig.weatherapp.weather.presentation.models.AirQualityUiModel
import github.detrig.weatherapp.weather.presentation.models.ParameterUi
import github.detrig.weatherapp.weather.presentation.models.WeatherInCityUi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Rule
import org.mockito.Mockito.mock
import java.time.LocalTime
import javax.inject.Inject
import javax.inject.Singleton

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class ScenarioTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createComposeRule()   //чтобы могли создавать composable в ui тестах

    @Inject
    lateinit var findCityRepository: FakeFindCityRepository
    @Inject
    lateinit var weatherRepository: FakeWeatherRepository

    @Before
    fun setUp() {
        hiltRule.inject()
    }

//    @Test
//    fun findCityAndShowWeather(): Unit = with(composeTestRule) {
//
//        setContent {
//            val navController: NavHostController = rememberNavController()
//            NavHost(navController = navController, startDestination = "findCityScreen") {
//                composable("findCityScreen") {
//                    FindCityScreen(
//                        viewModel = hiltViewModel(),
//                        navigateToWeatherScreen = {
//                            navController.navigate("weatherScreen")
//                        }
//                    )
//                }
//
//                composable("weatherScreen") {
//                    WeatherScreen(
//                        viewModel = hiltViewModel(),
//                        navigateToFindCityScreen = {
//                            navController.navigate("findCityScreen")
//                        },
//                        navigateToSettingsScreen = {
//                            navController.navigate("settingsScreen")
//                        }
//                    )
//                }
//
//                composable("settingsScreen") {
//                    SettingsScreen(
//                        viewModel = hiltViewModel(),
//                    )
//                }
//            }
//        }
//
//        startUiTest() //todo make fun startUiTestWithLoading() using runAsync returnResult()
//    }

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
                        foundCityScreenUiState = if (input.value.isEmpty())
                            FoundCityScreenUiState.Empty
                        else if (input.value == "FUCK")
                            if (shouldShowNoConnectionError.value) {
                                FoundCityScreenUiState.NoConnectionError
                            } else {
                                FoundCityScreenUiState.Empty
                            }
                        else
                            FoundCityScreenUiState.Base(
                                listOf(
                                    FoundCity(
                                        name = "Moscow",
                                        country = "Russia",
                                        latitude = 55.75f,
                                        longitude = 37.61f
                                    ),
                                    FoundCity(
                                        name = "Moscow",
                                        country = "USA",
                                        latitude = 55.75f,
                                        longitude = 37.61f
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
                        WeatherScreenUiState.NoConnectionError.Show(onRetryClick = {
                            shouldShowError.value = false
                        }, onSettingClick = {})
                    } else {
                        WeatherScreenUiState.Base(
                            weatherUi = WeatherInCityUi(
                                cityName = "Moscow",
                                temperature = "33.1",
                                feelTemperature = "31.2",
                                wind = "5.5",
                                uv = "0.4",
                                condition = "Sunny",
                                airQuality = AirQualityUiModel(
                                    title = R.string.harmful_for_sensitive_people,   // пример строки
                                    subtitle = "Людям с астмой лучше сократитsь время на улице",
                                    color = Color(0xFFFF9800), // оранжевый — Unhealthy for Sensitive Groups
                                    pm25 = ParameterUi(
                                        name = "PM2.5",
                                        value = "48 µg/m³",
                                        dangerLevel = R.string.high_level,
                                        icon = R.drawable.ic_pm25,
                                        color = Color(0xFFFF9800)
                                    ),
                                    pm10 = ParameterUi(
                                        name = "PM10",
                                        value = "59 µg/m³",
                                        dangerLevel = R.string.moderate_air,
                                        icon = R.drawable.ic_pm10,
                                        color = Color(0xFFFFC107)
                                    ),
                                    no2 = ParameterUi(
                                        name = "NO₂",
                                        value = "73 µg/m³",
                                        dangerLevel = R.string.moderate_air,
                                        icon = R.drawable.ic_no2,
                                        color = Color(0xFFFFC107)
                                    ),
                                    o3 = ParameterUi(
                                        name = "O₃",
                                        value = "4 µg/m³",
                                        dangerLevel = R.string.good_air,
                                        icon = R.drawable.ic_o3,
                                        color = Color(0xFF4CAF50)
                                    ),
                                    so2 = ParameterUi(
                                        name = "SO₂",
                                        value = "47 µg/m³",
                                        dangerLevel = R.string.moderate_air,
                                        icon = R.drawable.ic_so2,
                                        color = Color(0xFFFFC107)
                                    ),
                                    co = ParameterUi(
                                        name = "CO",
                                        value = "562 µg/m³",
                                        dangerLevel = R.string.good_air,
                                        icon = R.drawable.ic_co,
                                        color = Color(0xFF4CAF50)
                                    )
                                ),
                                forecast = emptyList(),
                                localTime = "22:00"
                            )
                        ).Show(onRetryClick = {
                            shouldShowError.value = false
                        }, onSettingClick = {})
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

    private fun createFakeWeather(): Weather {
        return Weather(
            localTime = LocalTime.now(),
            cityName = "Moscow",
            temperature = 10.0f,
            feelTemperature = 8.0f,
            windSpeed = 5.0f,
            uv = 3.0f,
            condition = "Sunny",
            airQuality = AirQuality(1f, 1f, 1f, 1f, 1f, 1f),
            forecastDay = emptyList()
        )
    }
}

