package github.detrig.weatherapp.weather.domain.models

import java.io.Serializable
import java.time.LocalTime

data class Weather(
    val localTime: LocalTime,
    val cityName: String,
    val temperature: Float,
    val feelTemperature: Float,
    val windSpeed: Float,
    val uv: Float,
    val condition: String,
    val airQuality: AirQuality,

    val forecastDay: List<ForecastDay>
) : Serializable