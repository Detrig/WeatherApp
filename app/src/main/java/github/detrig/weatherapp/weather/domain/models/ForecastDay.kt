package github.detrig.weatherapp.weather.domain.models

import java.io.Serializable
import java.time.LocalDate
import java.time.LocalTime

data class ForecastDay(
    val date: LocalDate,
    //val day: WeatherDay,
    val weatherForHour: List<WeatherForHour>
) : Serializable

data class WeatherDay(
    val maxTemp: Float,
    val minTemp: Float,
    val avgTemp: Float,
    val maxWind: Float,
    val dailyChanceOfRain: Float,
    val dailyChanceOfSnow: Float,
    val uv: Float
) : Serializable

data class WeatherForHour(
    val time: LocalTime,
    val temp: Float,
    val windSpeed: Float,
    val chanceOfRain: Float,
    val chanceOfSnow: Float,
    val cloud: Float
)