package github.detrig.weatherapp.weather.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import github.detrig.weatherapp.weather.domain.WeatherInCity
import java.io.Serializable

@Composable
fun WeatherScreen(viewModel: WeatherViewModel) {

    val weatherScreenUi = viewModel.state.collectAsStateWithLifecycle()
    weatherScreenUi.value.Show()
}

interface WeatherScreenUi : Serializable {

    @Composable
    fun Show()

    data object Empty : WeatherScreenUi {
        private fun readResolve(): Any = Empty

        @Composable
        override fun Show() = Unit
    }

    data class Base(private val cityParams: WeatherInCity) : WeatherScreenUi {

        @Composable
        override fun Show() {
            val backgroundModifier = backgroundForCondition(cityParams.condition)

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .then(backgroundModifier)
                    .testTag("WeatherBackground")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Text(
                        text = cityParams.cityName,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("City"),
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Row {
                        Text(
                            text = cityParams.temperature.toString(),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("Weather"),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = cityParams.feelTemperature.toString(),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("FeelTemp"),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = cityParams.windSpeed.toString(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("WindSpeed"),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = cityParams.uv.toString(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("Uv"),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun PreviewWeatherScreenUi() {
    WeatherScreenUi.Base(
        WeatherInCity(
            cityName = "Moscow city",
            temperature = 33.1f,
            feelTemperature = 31.2f,
            windSpeed = 5.5f,
            uv = 0.4f,
            condition = "sunny"
        )
    ).Show()
}

@Composable
private fun backgroundForCondition(condition: String): Modifier {
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
        .testTag(testTag)
}