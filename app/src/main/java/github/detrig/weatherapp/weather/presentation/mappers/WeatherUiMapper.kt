package github.detrig.weatherapp.weather.presentation.mappers

import github.detrig.weatherapp.weather.domain.WeatherResult
import github.detrig.weatherapp.weather.domain.models.WeatherInCity
import github.detrig.weatherapp.weather.presentation.WeatherScreenUiState
import github.detrig.weatherapp.weather.presentation.models.AirQualityUiMapper
import github.detrig.weatherapp.weather.presentation.models.WeatherInCityUi
import javax.inject.Inject

class WeatherUiMapper @Inject constructor(
    private val airQualityUiMapper: AirQualityUiMapper
) : WeatherResult.Mapper<WeatherScreenUiState> {

    override fun mapWeatherInCity(weather: WeatherInCity): WeatherScreenUiState {
        val uiModel = WeatherInCityUi(
            cityName = weather.cityName,
            temperature = "${weather.temperature}°",
            feelTemperature = "${weather.feelTemperature}°",
            wind = "${weather.windSpeed} м/с",
            uv = "${weather.uv}",
            condition = weather.condition,
            airQuality = airQualityUiMapper.map(weather.airQuality)
        )
        return WeatherScreenUiState.Base(uiModel)
    }

    override fun mapEmpty(): WeatherScreenUiState {
        return WeatherScreenUiState.Empty
    }

    override fun mapNoInternetError(): WeatherScreenUiState {
        return WeatherScreenUiState.NoConnectionError
    }

    override fun mapGenericError(): WeatherScreenUiState {
        return WeatherScreenUiState.GenericError
    }

    override fun mapLoading(): WeatherScreenUiState {
        return WeatherScreenUiState.Loading
    }

}