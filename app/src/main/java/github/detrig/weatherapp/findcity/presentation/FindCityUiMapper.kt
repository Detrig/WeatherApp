package github.detrig.weatherapp.findcity.presentation

import github.detrig.weatherapp.findcity.domain.FindCityResult
import github.detrig.weatherapp.findcity.domain.FoundCity
import javax.inject.Inject

class FindCityUiMapper @Inject constructor() : FindCityResult.Mapper<FoundCityUi> {

    override fun mapFoundCity(foundCity: List<FoundCity>): FoundCityUi {
        return FoundCityUi.Base(foundCity)
    }

    override fun mapEmpty(): FoundCityUi {
        return FoundCityUi.Empty
    }

    override fun mapLoading(): FoundCityUi {
        return FoundCityUi.Loading
    }

    override fun mapNoInternetError(): FoundCityUi {
        return FoundCityUi.NoConnectionError
    }
}