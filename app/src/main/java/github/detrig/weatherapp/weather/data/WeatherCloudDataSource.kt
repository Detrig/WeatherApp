package github.detrig.weatherapp.weather.data

import javax.inject.Inject

interface WeatherCloudDataSource {

    suspend fun getWeather(latitude: Float, longitude: Float): WeatherCloud

    class Base @Inject constructor(
        private val service: WeatherService
    ) : WeatherCloudDataSource {

        override suspend fun getWeather(
            latitude: Float,
            longitude: Float
        ): WeatherCloud {
            return service.getWeather("7f63ebcffd214161b8794516250611", "$latitude,$longitude")
        }
    }
}