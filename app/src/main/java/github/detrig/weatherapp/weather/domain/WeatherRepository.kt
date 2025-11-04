package github.detrig.weatherapp.weather.domain

interface WeatherRepository {

    suspend fun weather(): WeatherInCity
}