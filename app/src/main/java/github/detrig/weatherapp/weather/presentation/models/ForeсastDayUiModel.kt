package github.detrig.weatherapp.weather.presentation.models

import androidx.annotation.DrawableRes

data class ForecastDayUiModel(
    val date: String,
    val weatherForHour: List<WeatherForHourUi>
)

data class WeatherForHourUi(
    val time: String,
    val temp: String,
    val windSpeed: String,
    val chanceOfRain: String,
    val chanceOfSnow: String,
    val cloud: Float,
    @DrawableRes val iconForWeather: Int
)