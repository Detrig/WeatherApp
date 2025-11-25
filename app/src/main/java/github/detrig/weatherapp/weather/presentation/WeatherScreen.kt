package github.detrig.weatherapp.weather.presentation

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import github.detrig.weatherapp.R
import github.detrig.weatherapp.weather.presentation.models.AirQualityUi
import github.detrig.weatherapp.weather.presentation.models.ParameterUi
import github.detrig.weatherapp.weather.presentation.models.WeatherInCityUi

@Composable
fun WeatherScreen(viewModel: WeatherViewModel, navigateToFindCityScreen: () -> Unit) {

    val weatherScreenUi = viewModel.state.collectAsStateWithLifecycle()
    weatherScreenUi.value.Show(onRetryClick = viewModel::loadWeather)

    BackHandler {
        navigateToFindCityScreen.invoke()
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewWeatherNoInternetScreenUi() {
    WeatherScreenUiState.NoConnectionError
        .Show(onRetryClick = {})
}

@Preview(showBackground = true)
@Composable
fun PreviewWeatherScreenUi() {
    val airQualityMock = AirQualityUi(
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

    val weatherMock = WeatherInCityUi(
        cityName = "Moscow City",
        temperature = "33°",
        feelTemperature = "Ощущается как 31°",
        wind = "5.5 м/с",
        uv = "UV 4 (умеренный)",
        condition = "sunny",
        airQuality = airQualityMock
    )

    WeatherScreenUiState.Base(weatherMock).Show(onRetryClick = {})
}

