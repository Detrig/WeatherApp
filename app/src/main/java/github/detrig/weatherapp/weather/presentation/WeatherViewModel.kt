package github.detrig.weatherapp.weather.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import github.detrig.weatherapp.core.RunAsync
import github.detrig.weatherapp.weather.domain.WeatherRepository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository,
    private val savedStateHandle: SavedStateHandle,
    private val runAsync: RunAsync
) : ViewModel() {

    val state: StateFlow<WeatherScreenUi> =
        savedStateHandle.getStateFlow(KEY, WeatherScreenUi.Empty)

    init {
        runAsync.runAsync(viewModelScope, background = {
            val weatherInCity = repository.weather()
            WeatherScreenUi.Base(weatherInCity)
        }) {
            savedStateHandle[KEY] = it
        }
    }

    companion object {
        private const val KEY = "WeatherScreenUiKey"
    }
}
