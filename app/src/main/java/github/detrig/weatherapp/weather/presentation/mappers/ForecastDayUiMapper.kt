package github.detrig.weatherapp.weather.presentation.mappers

import androidx.annotation.DrawableRes
import github.detrig.weatherapp.R
import github.detrig.weatherapp.weather.domain.models.ForecastDay
import github.detrig.weatherapp.weather.domain.models.WeatherForHour
import github.detrig.weatherapp.weather.presentation.models.ForecastDayUiModel
import github.detrig.weatherapp.weather.presentation.models.WeatherForHourUi
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

interface ForecastDayUiMapper {
    fun map(domain: ForecastDay): ForecastDayUiModel

    class Base @Inject constructor() : ForecastDayUiMapper {

        private val formatter = DateTimeFormatter.ofPattern("d MM yyyy")

        override fun map(domain: ForecastDay): ForecastDayUiModel {
            return ForecastDayUiModel(
                date = domain.date.format(formatter),
                weatherForHour = domain.weatherForHour.map { it.toUiModel() }
            )
        }

        private fun WeatherForHour.toUiModel(): WeatherForHourUi {
            val formatter = DateTimeFormatter.ofPattern("HH:mm")
            return WeatherForHourUi(
                time = this.time.format(formatter),
                tempValue = this.temp,
                tempText = "${this.temp}°",
                windSpeed = "${this.windSpeed} km/h",
                chanceOfRain = "${this.chanceOfRain * 100}%",
                chanceOfSnow = "${this.chanceOfSnow * 100}%",
                cloud = this.cloud * 100,
                iconForWeather = mapHourlyWeatherIcon(
                    this.time,
                    this.chanceOfSnow,
                    this.chanceOfRain,
                    this.windSpeed,
                    this.cloud
                )
            )
        }

        @DrawableRes
        fun mapHourlyWeatherIcon(
            time: LocalTime,
            chanceOfSnow: Float,
            chanceOfRain: Float,
            windSpeed: Float,
            cloud: Float
        ): Int {
            // Ночь условно с 18 до 6
            val isNight = time.hour >= 18 || time.hour < 6

            val snowThreshold = 0.3f
            val rainThreshold = 0.3f
            val strongWindThreshold = 8f
            val highPrecipitation = 0.8f
            val mediumCloud = 0.50f
            val highCloud = 0.70f
            val veryHighCloud = 0.80f

            // 1. Снег имеет приоритет, если его шанс не меньше дождя
            val isSnowScenario = chanceOfSnow >= snowThreshold && chanceOfSnow >= chanceOfRain
            val isRainScenario = !isSnowScenario && chanceOfRain >= rainThreshold

            return when {
                // ---------- SNOW ----------
                isSnowScenario -> {
                    val hasStrongWind = windSpeed >= strongWindThreshold

                    if (hasStrongWind) {
                        if (cloud >= mediumCloud) {
                            R.drawable.wind_snow_cloudy50
                        } else {
                            R.drawable.wind_snow
                        }
                    } else {
                        if (chanceOfSnow >= highPrecipitation && cloud >= veryHighCloud) {
                            R.drawable.snow80_cloudy80
                        } else {
                            R.drawable.snowy_777629
                        }
                    }
                }

                // ---------- RAIN ----------
                isRainScenario -> {
                    val isHeavyRain = chanceOfRain >= highPrecipitation

                    if (cloud < mediumCloud) {
                        // Днём: дождь с солнцем, ночью: просто дождь
                        if (isNight) {
                            R.drawable.rainy80
                        } else {
                            R.drawable.rainy80_sunny50
                        }
                    } else {
                        // Пасмурно и дождь
                        if (isHeavyRain) {
                            R.drawable.rainy80
                        } else {
                            R.drawable.rainy80
                        }
                    }
                }

                // ---------- БЕЗ ОСАДКОВ ----------
                else -> {
                    when {
                        // Ясно: днём солнце, ночью — night.png
                        cloud < 0.30f -> {
                            if (isNight) {
                                R.drawable.night   // night.png
                            } else {
                                R.drawable.sunny
                            }
                        }

                        cloud < highCloud -> R.drawable.clody50 // cloudy50
                        else -> R.drawable.cloudy70
                    }
                }
            }
        }
    }
}

