package github.detrig.weatherapp.weather

import androidx.lifecycle.SavedStateHandle
import github.detrig.weatherapp.findcity.FakeRunAsync
import github.detrig.weatherapp.findcity.domain.NoInternetException
import github.detrig.weatherapp.findcity.presentation.FoundCityUi
import github.detrig.weatherapp.weather.domain.WeatherInCity
import github.detrig.weatherapp.weather.domain.WeatherRepository
import github.detrig.weatherapp.weather.domain.WeatherResult
import github.detrig.weatherapp.weather.presentation.WeatherScreenUi
import github.detrig.weatherapp.weather.presentation.WeatherUiMapper
import github.detrig.weatherapp.weather.presentation.WeatherViewModel
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.StateFlow
import org.junit.Before
import org.junit.Test


class WeatherViewModelTest {

    private lateinit var runAsync: FakeRunAsync
    private lateinit var repository: FakeWeatherRepository
    private lateinit var savedStateHandle: SavedStateHandle
    private lateinit var viewModel: WeatherViewModel
    private lateinit var weatherMapper: WeatherResult.Mapper<WeatherScreenUi>

    @Before
    fun setUp() {
        runAsync = FakeRunAsync()
        weatherMapper = WeatherUiMapper()
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
        val state: StateFlow<WeatherScreenUi> = viewModel.state
        assertEquals(WeatherScreenUi.Empty, state.value)

        val weatherForCity = WeatherInCity(
            cityName = "Moscow",
            temperature = 33.1f,
            feelTemperature = 31.2f,
            windSpeed = 5.5f,
            uv = 0.4f,
            condition = "Sunny"
        )

        runAsync.returnResult()
        assertEquals(WeatherScreenUi.NoConnectionError, state.value)

        viewModel.loadWeather()
        assertEquals(WeatherScreenUi.NoConnectionError, state.value)

        runAsync.returnResult()
        assertEquals(
            WeatherScreenUi.Base(weatherForCity), state.value
        )

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
