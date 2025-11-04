package github.detrig.weatherapp.weather.domain

import java.io.Serializable

data class WeatherInCity(
    val cityName: String,
    val temperature: String,
    val feelTemperature: String,
    val windSpeed: String
) : Serializable