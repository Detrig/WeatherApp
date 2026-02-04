package com.detrig.weather.presentation.mappers

import com.detrig.weather.domain.WeatherResult
import com.detrig.weather.domain.models.Weather
import com.detrig.weather.presentation.WeatherScreenUiState
import com.detrig.weather.presentation.models.WeatherInCityUi
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class WeatherUiMapper @Inject constructor(
    private val airQualityUiMapper: AirQualityUiMapper,
    private val forecastDayUiMapper: ForecastDayUiMapper
) : WeatherResult.Mapper<WeatherScreenUiState> {

    override fun mapWeatherInCity(weather: Weather): WeatherScreenUiState {
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        val uiModel = WeatherInCityUi(
            localTime = weather.localTime.format(formatter),
            cityName = weather.cityName,
            temperature = "${weather.temperature}°",
            feelTemperature = "${weather.feelTemperature}°",
            wind = "${weather.windSpeed} км/ч",
            uv = "${weather.uv}",
            condition = weather.condition,
            airQuality = airQualityUiMapper.map(weather.airQuality),
            forecast = weather.forecastDay.map { forecastDayUiMapper.map(it) }
        )
        return WeatherScreenUiState.Base(uiModel)
    }

    override fun mapEmpty(): WeatherScreenUiState {
        return WeatherScreenUiState.GenericError
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