package com.detrig.findcity.presentation.mappers

import com.detrig.core.model.FoundCity
import com.detrig.findcity.domain.FindCityResult
import com.detrig.findcity.presentation.FoundCityScreenUiState
import javax.inject.Inject

class FindCityUiMapper @Inject constructor() : FindCityResult.Mapper<FoundCityScreenUiState> {

    override fun mapFoundCity(foundCity: List<FoundCity>): FoundCityScreenUiState {
        return FoundCityScreenUiState.Base(foundCity)
    }

    override fun mapEmpty(): FoundCityScreenUiState {
        return FoundCityScreenUiState.Empty
    }

    override fun mapLoading(): FoundCityScreenUiState {
        return FoundCityScreenUiState.Loading
    }

    override fun mapNoInternetError(): FoundCityScreenUiState {
        return FoundCityScreenUiState.NoConnectionError
    }

    override fun mapGenericError(): FoundCityScreenUiState {
        return FoundCityScreenUiState.GenericError
    }
}