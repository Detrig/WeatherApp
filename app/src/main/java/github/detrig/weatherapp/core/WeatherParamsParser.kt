package github.detrig.weatherapp.core

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import github.detrig.weatherapp.R
import java.time.LocalTime

object WeatherParamsParser {
    @Composable
    internal fun backgroundForCondition(condition: String): Modifier {
        val lower = condition.lowercase()
        val color = when {
            "sunny" in lower -> Color(0xFFFFE082)
            "cloud" in lower -> Color(0xFF90A4AE)
            "rain" in lower -> Color(0xFF80CBC4)
            "snow" in lower -> Color(0xFFE0F7FA)
            "storm" in lower -> Color(0xFF616161)
            else -> Color(0xFFB0BEC5)
        }

        val testTag = when {
            "sunny" in lower -> "SunnyBackground"
            "cloud" in lower -> "CloudyBackground"
            "rain" in lower -> "RainBackground"
            "snow" in lower -> "SnowBackground"
            "storm" in lower -> "StormBackground"
            else -> "DefaultBackground"
        }

        return Modifier
            .background(color)
            //.paint(painter = painterResource(R.drawable.no_internet), contentScale = ContentScale.Crop)
            .testTag(testTag)
    }

    @Composable
    internal fun backgroundColorForCondition(condition: String?): Color {
        val lower = condition?.lowercase() ?: "rain"
        val color = when {
            "sunny" in lower -> Color(0xFFFFE082)
            "cloud" in lower -> Color(0xFF90A4AE)
            "rain" in lower -> Color(0xFF80CBC4)
            "snow" in lower -> Color(0xFFE0F7FA)
            "storm" in lower -> Color(0xFF616161)
            else -> Color(0xFFB0BEC5)
        }

        return color
    }

    @DrawableRes
    fun mapCurrentWeatherIcon(
        time: LocalTime,
        temperature: Float,
        condition: String,
        windSpeed: Float
    ): Int {
        val isNight = time.hour >= 18 || time.hour < 6

        val strongWindThreshold = 8f

        val text = condition.lowercase()

        val isSnowText = listOf("snow", "blizzard", "sleet", "ice", "freezing")
            .any { it in text }

        val isRainText = listOf("rain", "drizzle", "shower")
            .any { it in text }

        val isStormText = listOf("thunder", "storm", "tstorm")
            .any { it in text }

        val isCloudyText = listOf("cloud", "overcast")
            .any { it in text }

        val isClearText = listOf("sunny", "clear")
            .any { it in text }

        val isPartly = "partly" in text || "partially" in text

        // Немного эвристики для снега:
        // если холодно и написано про rain, можно считать мокрым снегом
        val isCold = temperature <= 0f
        val isSnow = isSnowText || (isCold && isRainText)

        val isRain = !isSnow && (isRainText || isStormText)

        return when {
            // ---------- SNOW ----------
            isSnow -> {
                val hasStrongWind = windSpeed >= strongWindThreshold

                if (hasStrongWind) {
                    // Сильный ветер + снег
                    if (isCloudyText) {
                        R.drawable.wind_snow_cloudy50
                    } else {
                        R.drawable.wind_snow
                    }
                } else {
                    // Обычный снег
                    if (isCloudyText) {
                        R.drawable.snow80_cloudy80
                    } else {
                        R.drawable.snowy_777629
                    }
                }
            }

            // ---------- RAIN / STORM ----------
            isRain -> {
                val hasStrongWind = windSpeed >= strongWindThreshold

                // Если есть солнце (днём и не супер-пасмурно) — дождь + солнце
                if (!isNight && !isCloudyText && !hasStrongWind) {
                    R.drawable.rainy80_sunny50
                } else {
                    // Ночь / пасмурно / ветер — обычная иконка дождя
                    R.drawable.rainy80
                }
            }

            // ---------- БЕЗ ОСАДКОВ ----------
            else -> {
                when {
                    // Ясно
                    isClearText && !isCloudyText -> {
                        if (isNight) {
                            R.drawable.night
                        } else {
                            R.drawable.sunny
                        }
                    }

                    // Частично облачно
                    isCloudyText && isPartly -> {
                        R.drawable.clody50
                    }

                    // Просто облачно
                    isCloudyText -> {
                        R.drawable.cloudy70
                    }

                    // Ничего понятного из condition не вытащили — fallback
                    else -> {
                        if (isNight) {
                            R.drawable.night
                        } else {
                            R.drawable.sunny
                        }
                    }
                }
            }
        }
    }

}