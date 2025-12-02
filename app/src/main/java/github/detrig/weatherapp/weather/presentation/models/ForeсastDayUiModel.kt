package github.detrig.weatherapp.weather.presentation.models

import androidx.annotation.DrawableRes
import java.io.Serializable

data class ForecastDayUiModel(
    val date: String,
    val weatherForHour: List<WeatherForHourUi>
) : Serializable

data class WeatherForHourUi(
    val time: String,
    val tempValue: Float,
    val tempText: String,
    val windSpeed: String,
    val chanceOfRain: String,
    val chanceOfSnow: String,
    val cloud: Float,
    @DrawableRes val iconForWeather: Int = 0
) : Serializable