package github.detrig.weatherapp.weather.domain.models

import java.io.Serializable

data class AirQuality(
    val co: Float,
    val no2: Float,
    val o3: Float,
    val so2: Float,
    val pm25: Float,
    val pm10: Float
) : Serializable