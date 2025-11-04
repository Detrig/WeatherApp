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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun WeatherScreen(viewModel: WeatherViewModel) {

    val weatherScreenUi = viewModel.state.collectAsStateWithLifeCycle()
    weatherScreenUi.value.Show()
}

interface WeatherScreenUi {

    @Composable
    fun Show()

    data class Base(private val cityParams: CityParams) : WeatherScreenUi {

        @Composable
        override fun Show() {
            Column {
                Text(
                    text = cityParams.cityName,
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row {
                    Text(
                        text = cityParams.temperature,
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = cityParams.feelTemperature,
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(text = cityParams.windSpeed, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
            }
        }

    }
}