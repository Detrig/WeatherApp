package github.detrig.weatherapp.weather.presentation

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import github.detrig.weatherapp.core.RunAsync
import github.detrig.weatherapp.core.network.NetworkStatus
import github.detrig.weatherapp.core.network.NetworkStatusRepository
import github.detrig.weatherapp.weather.domain.WeatherRepository
import github.detrig.weatherapp.weather.domain.WeatherResult
import github.detrig.weatherapp.weather.domain.schedule.WeatherUpdateScheduler
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherUiMapper: WeatherResult.Mapper<WeatherScreenUiState>,
    private val repository: WeatherRepository,
    private val weatherUpdateScheduler: WeatherUpdateScheduler,
    private val networkStatusRepository: NetworkStatusRepository,
    private val savedStateHandle: SavedStateHandle,
    private val runAsync: RunAsync
) : ViewModel() {

    val state: StateFlow<WeatherScreenUiState> =
        repository.observeWeather()
            .onEach { result ->
                Log.d("alz-04", "DB emission: $result")
            }
            .map { it.map(weatherUiMapper) }
            .stateIn(
                viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = weatherUiMapper.mapLoading()
            )

    init {
        loadWeather()
        weatherUpdateScheduler.schedulePeriodicUpdate()
        observeNetworkStatus()
        //onDebugClickRunWorkerOnce()
    }

    fun loadWeather() {
        runAsync.runAsync(viewModelScope, background = {
            val weatherResult = repository.weather()
            weatherResult.map(weatherUiMapper)
        }) {
//            savedStateHandle[KEY] = it
        }
    }

    private fun observeNetworkStatus() {
        networkStatusRepository.observeNetworkStatus()
            .onEach { status ->
                Log.d("alz-04", "network status: $status")
                if (status is NetworkStatus.Available) {
                    loadWeather()
                }
            }
            .launchIn(viewModelScope)
    }

    fun onDebugClickRunWorkerOnce() {
        weatherUpdateScheduler.scheduleOneTimeDebugUpdate()
    }

    companion object {
        private const val KEY = "WeatherScreenUiKey"
    }
}
