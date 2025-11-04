package github.detrig.weatherapp.weather

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.serialization.Serializable

@Composable
fun WeatherScreen(viewModel: WeatherViewModel) {

    val weatherScreenUi = viewModel.state.collectAsStateWithLifeCycle()
    weatherScreenUi.value.Show()
}

interface WeatherScreenUi : Serializable {

    @Composable
    fun Show()

    data object Empty : WeatherScreenUi {
        @Composable
        override fun Show() = Unit
    }

    data class Base(private val cityParams: WeatherInCity) : WeatherScreenUi {

        @Composable
        override fun Show() {
            Column {
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
                        text = cityParams.temperature,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("Weather"),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = cityParams.feelTemperature,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("FeelTemp"),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = cityParams.windSpeed,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("WindSpeed"),
                    textAlign = TextAlign.Center
                )
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
            temperature = "33.1",
            feelTemperature = "31.2",
            windSpped = "5.5"
        )
    )
}