package github.detrig.weatherapp.weather.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import github.detrig.weatherapp.core.RunAsync
import github.detrig.weatherapp.weather.domain.WeatherRepository
import github.detrig.weatherapp.weather.domain.WeatherResult
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherUiMapper: WeatherResult.Mapper<WeatherScreenUiState>,
    private val repository: WeatherRepository,
    private val savedStateHandle: SavedStateHandle,
    private val runAsync: RunAsync
) : ViewModel() {

    val state: StateFlow<WeatherScreenUiState> =
        repository.observeWeather()
            .map { it.map(weatherUiMapper) }
            .stateIn(
                viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = weatherUiMapper.mapLoading()
            )

    init {
        loadWeather()
    }

    fun loadWeather() {
        runAsync.runAsync(viewModelScope, background = {
            val weatherResult = repository.weather()
            weatherResult.map(weatherUiMapper)
        }) {
            savedStateHandle[KEY] = it
        }
    }

    companion object {
        private const val KEY = "WeatherScreenUiKey"
    }
}
