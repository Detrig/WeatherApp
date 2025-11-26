package github.detrig.weatherapp.weather.presentation.models

data class WeatherInCityUi(
    val cityName: String,
    val temperature: String,
    val feelTemperature: String,
    val wind: String,
    val uv: String,
    val condition: String,
    val airQuality: AirQualityUiModel
)