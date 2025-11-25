package github.detrig.weatherapp.weather.presentation.models

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color

data class AirQualityUi(
    @StringRes val title: Int,
    val subtitle: String,
    val color: Color,
    val pm25: ParameterUi,
    val pm10: ParameterUi,
    val no2: ParameterUi,
    val o3: ParameterUi,
    val so2: ParameterUi,
    val co: ParameterUi
)

data class ParameterUi(
    val name: String,
    val value: String,
    @StringRes val dangerLevel: Int,
    @DrawableRes val icon: Int,
    val color: Color
)