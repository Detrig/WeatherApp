package github.detrig.weatherapp.core

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag

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
}