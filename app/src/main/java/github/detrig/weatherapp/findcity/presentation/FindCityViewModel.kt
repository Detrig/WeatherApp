package github.detrig.weatherapp.findcity.presentation

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import github.detrig.weatherapp.core.RunAsync
import github.detrig.weatherapp.findcity.domain.FindCityRepository
import github.detrig.weatherapp.findcity.domain.FindCityResult
import github.detrig.weatherapp.findcity.domain.FoundCity
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class FindCityViewModel @Inject constructor(
    private val mapper: FindCityResult.Mapper<FoundCityUi>,
    private val repository: FindCityRepository,
    private val savedStateHandle: SavedStateHandle,
    private val runAsync: RunAsync
) : ViewModel() {

    val state: StateFlow<FoundCityUi> =
        savedStateHandle.getStateFlow(KEY, mapper.mapEmpty())

    fun findCity(cityName: String) {
        if (cityName.trim().isEmpty())
            savedStateHandle[KEY] = mapper.mapEmpty()
        else
            runAsync.runAsync(viewModelScope, {
                savedStateHandle[KEY] = mapper.mapLoading()
                val foundCity = repository.findCity(cityName).map(mapper)
                foundCity
            }) {
                savedStateHandle[KEY] = it
            }
    }

    fun saveChosenCity(foundCity: FoundCity) {
        runAsync.runAsync(viewModelScope, background = {
            repository.saveCity(foundCity)
        }) {

        }
    }

    fun findCityByLocation(lat: Double, lon: Double, onComplete: () -> Unit) {
        Log.d("alz-04", "lat: $lat, lon: $lon")
        runAsync.runAsync(viewModelScope, background = {
            savedStateHandle[KEY] = mapper.mapLoading()

            val foundCityResult = repository.findCity("$lat,$lon")

            if (foundCityResult is FindCityResult.Base && foundCityResult.foundCity.isNotEmpty()) {
                val foundCityByGeo = foundCityResult.foundCity.first()
                repository.saveCity(foundCityByGeo)
            }
            foundCityResult.map(mapper)
        }) {
            savedStateHandle[KEY] = it
            onComplete.invoke()
        }
    }

    companion object {
        private const val KEY = "FoundCityUiKey"
    }
}