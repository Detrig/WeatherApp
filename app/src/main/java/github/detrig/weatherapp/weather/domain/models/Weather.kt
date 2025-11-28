package github.detrig.weatherapp.weather.domain.models

import java.io.Serializable

data class Weather(
    val cityName: String,
    val temperature: Float,
    val feelTemperature: Float,
    val windSpeed: Float,
    val uv: Float,
    val condition: String,
    val airQuality: AirQuality,

    val forecastDay: List<ForecastDay>
) : Serializable