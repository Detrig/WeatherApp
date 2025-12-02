package github.detrig.weatherapp

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.Color
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
import github.detrig.weatherapp.findcity.domain.FindCityResult
import github.detrig.weatherapp.findcity.domain.models.FoundCity
import github.detrig.weatherapp.core.NoInternetException
import github.detrig.weatherapp.findcity.presentation.FindCityScreen
import github.detrig.weatherapp.findcity.presentation.FindCityScreenUi
import github.detrig.weatherapp.findcity.presentation.mappers.FindCityUiMapper
import github.detrig.weatherapp.findcity.presentation.FindCityViewModel
import github.detrig.weatherapp.findcity.presentation.FoundCityScreenUiState
import github.detrig.weatherapp.weather.domain.models.Weather
import github.detrig.weatherapp.weather.domain.WeatherRepository
import github.detrig.weatherapp.weather.domain.WeatherResult
import github.detrig.weatherapp.weather.domain.models.AirQuality
import github.detrig.weatherapp.weather.presentation.WeatherScreen
import github.detrig.weatherapp.weather.presentation.WeatherScreenUiState
import github.detrig.weatherapp.weather.presentation.mappers.WeatherUiMapper
import github.detrig.weatherapp.weather.presentation.WeatherViewModel
import github.detrig.weatherapp.weather.presentation.mappers.AirQualityUiMapper
import github.detrig.weatherapp.weather.presentation.models.AirQualityUiModel
import github.detrig.weatherapp.weather.presentation.models.ParameterUi
import github.detrig.weatherapp.weather.presentation.models.WeatherInCityUi
import kotlinx.coroutines.CoroutineScope
import org.junit.Before

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Rule

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class ScenarioTest {

    @get:Rule
    val composeTestRule = createComposeRule()   //чтобы могли создавать composable в ui тестах

    private lateinit var fakeRunAsync: FakeRunAsync

    @Before
    fun setUp() {
        fakeRunAsync = FakeRunAsync()
    }

    @Test
    fun findCityAndShowWeather(): Unit = with(composeTestRule) {
        val findCityMapper: FindCityResult.Mapper<FoundCityScreenUiState> = FindCityUiMapper()
        val findCityViewModel = FindCityViewModel(
            mapper = findCityMapper,
            repository = FakeFindCityRepository(),
            savedStateHandle = SavedStateHandle(),
            runAsync = fakeRunAsync
        )

        val weatherMapper: WeatherResult.Mapper<WeatherScreenUiState> = WeatherUiMapper(
            AirQualityUiMapper.Base()
        )
        val weatherViewModel = WeatherViewModel(
            weatherUiMapper = weatherMapper,
            repository = FakeWeatherRepository(),
            savedStateHandle = SavedStateHandle(),
            runAsync = fakeRunAsync
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
                        viewModel = weatherViewModel,
                        navigateToFindCityScreen = {
                            navController.navigate("findCityScreen")
                        }
                    )
                }
            }
        }

        startUiTest() //todo make fun startUiTestWithLoading() using runAsync returnResult()
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
                        })
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
                                )
                            )
                        ).Show(onRetryClick = {
                            shouldShowError.value = false
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

        if (query == "FUCK" && shouldShowError) {
            shouldShowError = false
            return FindCityResult.Failed(error = NoInternetException)
        } else if (query == "FUCK" && !shouldShowError) {
            return FindCityResult.Empty
        }

        if (query == "Mos")
            return FindCityResult.Base(
                listOf(
                    FoundCity(
                        name = "Moscow",
                        latitude = 55.75f,
                        country = "Russia",
                        longitude = 37.61f
                    ),
                    FoundCity(
                        name = "Moscow",
                        country = "USA",
                        latitude = 55.75f,
                        longitude = 37.61f
                    )
                )
            )

        throw IllegalStateException("not supported for this test")
    }

    override suspend fun saveCity(foundCity: FoundCity) {
        if (foundCity != FoundCity(
                name = "Moscow",
                country = "Russia",
                latitude = 55.75f,
                longitude = 37.61f
            )
        )
            throw IllegalStateException("save called with wrong argument $foundCity")
    }
}

private class FakeWeatherRepository : WeatherRepository {

    private var shouldShowError = true
    private var savedCity: FoundCity? = null

    override suspend fun weather(): WeatherResult {
        if (shouldShowError) {
            shouldShowError = false
            return WeatherResult.Failed(error = NoInternetException)
        } else {
            return WeatherResult.Base(
                Weather(
                    cityName = "Moscow",
                    temperature = 33.1f,
                    feelTemperature = 31.2f,
                    windSpeed = 5.5f,
                    uv = 0.4f,
                    condition = "Sunny",
                    airQuality = AirQuality(1f, 1f, 1f, 1f, 1f, 1f)
                )
            )
        }
    }

    override suspend fun getSavedCity(): FoundCity {
        savedCity = FoundCity(
            name = "Moscow",
            latitude = 55.75f,
            country = "Russia",
            longitude = 37.61f
        )
        return savedCity!!
    }
}

class FakeRunAsync : RunAsync {

    private var backgroundCached: (suspend () -> Any)? = null
    private var uiCached: ((Any) -> Unit)? = null

    override fun <T : Any> runAsync(
        scope: CoroutineScope,
        background: suspend () -> T,
        ui: (T) -> Unit
    ) {
        backgroundCached = background
        uiCached = { any -> ui(any as T) }
    }

    suspend fun returnResult() {
        val bg = backgroundCached ?: error("Background not set")
        val ui = uiCached ?: error("Ui not set")
        val result = bg()
        ui(result)
    }
}