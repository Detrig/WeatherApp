package github.detrig.weatherapp.weather

import androidx.lifecycle.SavedStateHandle
import github.detrig.weatherapp.findcity.FakeRunAsync
import github.detrig.weatherapp.findcity.FindCityViewModel
import github.detrig.weatherapp.findcity.FoundCityUi
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.StateFlow
import org.junit.Before
import org.junit.Test


class WeatherViewModelTest {

    private lateinit var runAsync: FakeRunAsync
    private lateinit var repository: FakeWeatherRepository
    private lateinit var savedStateHandle: SavedStateHandle
    private lateinit var viewModel: WeatherViewModel

    @Before
    fun setUp() {
        runAsync = FakeRunAsync()
        savedStateHandle = SavedStateHandle()
        repository = FakeWeatherRepository()
        viewModel = WeatherViewModel(
            savedStateHandle = savedStateHandle,
            repository = repository,
            runAsync = runAsync
        )
    }

    @Test
    fun getWeatherInCity() {
        val state: StateFlow<WeatherScreenUi> = viewModel.state
        assertEquals(WeatherScreenUi.Empty, state.value)

        val weatherForCity = WeatherInCity(
            cityName = "Moscow city",
            temperature = "33.1",
            feelTemperature = "31.2",
            windSpped = "5.5"
        )

        runAsync.returnResult()
        assertEquals(
            WeatherScreenUi.Base(weatherForCity), state.value
        )

    }
}

private class FakeWeatherRepository : WeatherRepository {

    override suspend fun weather(): WeatherForCity {
        return WeatherInCity(
            cityName = "Moscow city",
            temperature = "33.1",
            feelTemperature = "31.2",
            windSpped = "5.5"
        )
    }
}
