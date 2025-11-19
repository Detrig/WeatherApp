package github.detrig.weatherapp.findcity.presentation

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

    companion object {
        private const val KEY = "FoundCityUiKey"
    }
}