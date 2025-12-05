package github.detrig.weatherapp.weather.presentation.models

import java.io.Serializable

data class WeatherInCityUi(
    val localTime: String,
    val cityName: String,
    val temperature: String,
    val feelTemperature: String,
    val wind: String,
    val uv: String,
    val condition: String,
    val airQuality: AirQualityUiModel,
    val forecast: List<ForecastDayUiModel>
) : Serializable