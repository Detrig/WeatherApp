package github.detrig.weatherapp.weather.domain

import android.util.Log
import github.detrig.weatherapp.core.DomainException
import github.detrig.weatherapp.findcity.domain.models.FoundCity
import github.detrig.weatherapp.weather.data.WeatherCachedDataSource
import github.detrig.weatherapp.weather.data.WeatherCloudDataSource
import github.detrig.weatherapp.weather.data.mappers.toDomain
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
                    result.toDomain(foundCity.name)
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