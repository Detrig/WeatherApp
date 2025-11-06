package github.detrig.weatherapp.weather.domain

import java.io.Serializable

data class WeatherInCity(
    val cityName: String,
    val temperature: Float,
    val feelTemperature: Float,
    val windSpeed: Float,
    val uv: Float,
    val condition: String
) : Serializable