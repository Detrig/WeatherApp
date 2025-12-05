package github.detrig.weatherapp.weather.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import github.detrig.weatherapp.R
import github.detrig.weatherapp.weather.presentation.models.ForecastDayUiModel
import github.detrig.weatherapp.weather.presentation.models.WeatherForHourUi

@Composable
fun ForecastUi(
    forecastUi: List<ForecastDayUiModel>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        forecastUi.forEach { day ->
            ForecastDayItem(day)
            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
private fun ForecastDayItem(day: ForecastDayUiModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        Text(
            text = day.date,
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(Modifier.height(8.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(day.weatherForHour) { hour ->
                HourWeatherItem(hour)
            }
        }
    }
}

@Composable
private fun HourWeatherItem(hour: WeatherForHourUi) {
    Column(
        modifier = Modifier.width(80.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = hour.time, style = MaterialTheme.typography.labelSmall)

        Spacer(Modifier.height(4.dp))

        Image(
            painter = painterResource(hour.iconForWeather),
            contentDescription = null,
            modifier = Modifier.size(32.dp)
        )

        Spacer(Modifier.height(4.dp))

        Text(text = hour.tempText, style = MaterialTheme.typography.bodySmall)

        Spacer(Modifier.height(2.dp))
        Column(modifier = Modifier.align(Alignment.Start)) {
            Text(text = "Ветер: ${hour.windSpeed}", style = MaterialTheme.typography.labelSmall)
            Text(text = "Дождь: ${hour.chanceOfRain}", style = MaterialTheme.typography.labelSmall)
            Text(text = "Снег: ${hour.chanceOfSnow}", style = MaterialTheme.typography.labelSmall)
            Text(
                text = "Облака: ${hour.cloud.toInt()}%",
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewForecastUi() {
    ForecastUi(
        forecastUi = listOf(
            ForecastDayUiModel(
                date = "02.12.2025",
                weatherForHour = listOf(
                    WeatherForHourUi(
                        time = "22:00",
                        tempValue = 0f,
                        tempText = "0*",
                        windSpeed = "5f",
                        chanceOfRain = "",
                        iconForWeather = R.drawable.ic_o3,
                        chanceOfSnow = "",
                        cloud = 0f
                    ),
                    WeatherForHourUi(
                        time = "23:00",
                        tempValue = 2f,
                        tempText = "2*",
                        windSpeed = "5f",
                        chanceOfRain = "",
                        chanceOfSnow = "",
                        iconForWeather = R.drawable.ic_o3,
                        cloud = 0f
                    ),
                    WeatherForHourUi(
                        time = "24:00",
                        tempValue = 2f,
                        tempText = "2*",
                        windSpeed = "5f",
                        iconForWeather = R.drawable.ic_o3,
                        chanceOfRain = "",
                        chanceOfSnow = "",
                        cloud = 0f
                    )
                )
            )
        )
    )
}