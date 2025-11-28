package github.detrig.weatherapp.weather.presentation.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import github.detrig.weatherapp.R
import github.detrig.weatherapp.weather.presentation.models.AirQualityUiModel
import github.detrig.weatherapp.weather.presentation.models.ParameterUi
import github.detrig.weatherapp.weather.presentation.models.WeatherInCityUi

@Composable
fun WeatherUi(cityParams: WeatherInCityUi) {

    Box(
        modifier = Modifier
            .testTag("WeatherBackground")
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .border(
                    width = 1.dp,
                    color = Color.Gray,
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(8.dp)
        ) {
            Text(
                text = cityParams.cityName,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("City"),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )

            Text(
                text = cityParams.localTime,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("LocalTime"),
                style = MaterialTheme.typography.labelSmall,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row {
                Column(
                    modifier = Modifier
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.temp),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodySmall,
                        fontSize = 10.sp

                    )
                    Text(
                        text = cityParams.temperature,
                        modifier = Modifier.testTag("Weather"),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                Column(
                    modifier = Modifier
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.feelTemp),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodySmall,
                        fontSize = 10.sp
                    )
                    Text(
                        text = cityParams.feelTemperature,
                        modifier = Modifier.testTag("FeelTemp"),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = stringResource(R.string.windSpeed),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodySmall,
                fontSize = 10.sp
            )
            Text(
                text = cityParams.wind,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("WindSpeed"),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.uv),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodySmall,
                fontSize = 10.sp
            )
            Text(
                text = cityParams.uv,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("Uv"),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun WeatherUiPreview() {
    val airQualityMock = AirQualityUiModel(
        title = R.string.harmful_for_sensitive_people,   // пример строки
        subtitle = "Людям с астмой лучше сократитsь время на улице",
        color = Color(0xFFFF9800), // оранжевый — Unhealthy for Sensitive Groups
        pm25 = ParameterUi(
            name = "PM2.5",
            value = "48 µg/m³",
            dangerLevel = R.string.high_level,
            icon = R.drawable.ic_pm25,
            color = Color(0xFFFF9800)
        ),
        pm10 = ParameterUi(
            name = "PM10",
            value = "59 µg/m³",
            dangerLevel = R.string.moderate_air,
            icon = R.drawable.ic_pm10,
            color = Color(0xFFFFC107)
        ),
        no2 = ParameterUi(
            name = "NO₂",
            value = "73 µg/m³",
            dangerLevel = R.string.moderate_air,
            icon = R.drawable.ic_no2,
            color = Color(0xFFFFC107)
        ),
        o3 = ParameterUi(
            name = "O₃",
            value = "4 µg/m³",
            dangerLevel = R.string.good_air,
            icon = R.drawable.ic_o3,
            color = Color(0xFF4CAF50)
        ),
        so2 = ParameterUi(
            name = "SO₂",
            value = "47 µg/m³",
            dangerLevel = R.string.moderate_air,
            icon = R.drawable.ic_so2,
            color = Color(0xFFFFC107)
        ),
        co = ParameterUi(
            name = "CO",
            value = "562 µg/m³",
            dangerLevel = R.string.good_air,
            icon = R.drawable.ic_co,
            color = Color(0xFF4CAF50)
        )
    )

    WeatherUi(
        WeatherInCityUi(
            localTime = "22:00",
            cityName = "Moscow City",
            temperature = "33°",
            feelTemperature = "31°",
            wind = "5.5 м/с",
            uv = "4",
            condition = "sunny",
            airQuality = airQualityMock,
            forecast = listOf()
        )
    )
}