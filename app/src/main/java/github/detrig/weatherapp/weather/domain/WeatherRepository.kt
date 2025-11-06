package github.detrig.weatherapp.weather.domain

import android.util.Log
import github.detrig.weatherapp.weather.data.WeatherCachedDataSource
import github.detrig.weatherapp.weather.data.WeatherCloudDataSource
import javax.inject.Inject

interface WeatherRepository {

    suspend fun weather(): WeatherInCity

    class Base @Inject constructor(
        private val cachedDataSource: WeatherCachedDataSource,
        private val weatherCloudDataSource: WeatherCloudDataSource
    ) : WeatherRepository {

        override suspend fun weather(): WeatherInCity {
            val (latitude, longitude, cityName) = cachedDataSource.getCityParams()
            val result = weatherCloudDataSource.getWeather(latitude, longitude)
            Log.d("alz-04", "result: $result")
            return WeatherInCity(
                cityName = cityName,
                temperature = result.currentWeather.temp,
                feelTemperature = result.currentWeather.feelTemp,
                windSpeed = result.currentWeather.windSpeed,
                uv = result.currentWeather.uv,
                condition = result.currentWeather.condition.text
            )
        }

    }
}