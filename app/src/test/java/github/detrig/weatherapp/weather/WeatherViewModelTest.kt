package github.detrig.weatherapp.weather

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.SavedStateHandle
import github.detrig.weatherapp.R
import github.detrig.weatherapp.findcity.FakeRunAsync
import github.detrig.weatherapp.findcity.domain.models.FoundCity
import github.detrig.weatherapp.core.NoInternetException
import github.detrig.weatherapp.weather.domain.models.Weather
import github.detrig.weatherapp.weather.domain.WeatherRepository
import github.detrig.weatherapp.weather.domain.WeatherResult
import github.detrig.weatherapp.weather.domain.models.AirQuality
import github.detrig.weatherapp.weather.presentation.WeatherScreenUiState
import github.detrig.weatherapp.weather.presentation.mappers.WeatherUiMapper
import github.detrig.weatherapp.weather.presentation.WeatherViewModel
import github.detrig.weatherapp.weather.presentation.mappers.AirQualityUiMapper
import github.detrig.weatherapp.weather.presentation.models.AirQualityUiModel
import github.detrig.weatherapp.weather.presentation.models.ParameterUi
import github.detrig.weatherapp.weather.presentation.models.WeatherInCityUi
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.StateFlow
import org.junit.Before
import org.junit.Test


class WeatherViewModelTest {

    private lateinit var runAsync: FakeRunAsync
    private lateinit var repository: FakeWeatherRepository
    private lateinit var savedStateHandle: SavedStateHandle
    private lateinit var viewModel: WeatherViewModel
    private lateinit var airQualityUiMapper: AirQualityUiMapper
    private lateinit var weatherMapper: WeatherResult.Mapper<WeatherScreenUiState>

    @Before
    fun setUp() {
        runAsync = FakeRunAsync()
        airQualityUiMapper = AirQualityUiMapper.Base()
        weatherMapper = WeatherUiMapper(airQualityUiMapper)
        savedStateHandle = SavedStateHandle()
        repository = FakeWeatherRepository()
        viewModel = WeatherViewModel(
            weatherUiMapper = weatherMapper,
            savedStateHandle = savedStateHandle,
            repository = repository,
            runAsync = runAsync
        )
    }

    @Test
    fun getErrorThenGetWeatherInCity() {
        val state: StateFlow<WeatherScreenUiState> = viewModel.state
        assertEquals(WeatherScreenUiState.Empty, state.value)

        val weatherForCityUi = WeatherInCityUi(
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

        runAsync.returnResult()
        assertEquals(WeatherScreenUiState.NoConnectionError, state.value)

        viewModel.loadWeather()
        assertEquals(WeatherScreenUiState.NoConnectionError, state.value)

        runAsync.returnResult()
        assertEquals(
            WeatherScreenUiState.Base(weatherForCityUi), state.value
        )

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
