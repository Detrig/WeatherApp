package github.detrig.weatherapp.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import github.detrig.weatherapp.core.RunAsync
import github.detrig.weatherapp.weather.domain.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val runAsync: RunAsync
) : ViewModel() {

    private val _startDestination = MutableStateFlow<String?>(null)
    val startDestination: StateFlow<String?> = _startDestination

    init {
        checkSavedCity()
    }

    private fun checkSavedCity() {
        runAsync.runAsync(viewModelScope, background = {
            weatherRepository.getSavedCity()
        }) { savedCity ->
            val destination = if (savedCity.name.isNotBlank()) {
                "weatherScreen"
            } else {
                "findCityScreen"
            }
            _startDestination.value = destination
        }
    }
}