package github.detrig.weatherapp.weather.domain

import android.util.Log
import github.detrig.weatherapp.findcity.domain.DomainException
import github.detrig.weatherapp.findcity.domain.FindCityResult
import github.detrig.weatherapp.findcity.domain.FoundCity
import github.detrig.weatherapp.weather.data.WeatherCachedDataSource
import github.detrig.weatherapp.weather.data.WeatherCloudDataSource
import javax.inject.Inject

interface WeatherRepository {

    suspend fun weather(): WeatherResult
    suspend fun getSavedCity(): FoundCity

    class Base @Inject constructor(
        private val cachedDataSource: WeatherCachedDataSource,
        private val weatherCloudDataSource: WeatherCloudDataSource
    ) : WeatherRepository {

        override suspend fun weather(): WeatherResult {
            try {
                val foundCity = cachedDataSource.getCityParams()
                val result =
                    weatherCloudDataSource.getWeather(foundCity.latitude, foundCity.longitude)
                Log.d("alz-04", "result: $result")
                return WeatherResult.Base(
                    WeatherInCity(
                        cityName = foundCity.name,
                        temperature = result.currentWeather.temp,
                        feelTemperature = result.currentWeather.feelTemp,
                        windSpeed = result.currentWeather.windSpeed,
                        uv = result.currentWeather.uv,
                        condition = result.currentWeather.condition.text
                    )
                )
            } catch (e: DomainException) {
                return WeatherResult.Failed(e)
            }
        }

        override suspend fun getSavedCity(): FoundCity {
            return cachedDataSource.getCityParams()
        }
    }
}