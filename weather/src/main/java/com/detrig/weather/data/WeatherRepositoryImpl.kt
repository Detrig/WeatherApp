package com.detrig.weather.data

import android.util.Log
import com.google.gson.Gson
import com.detrig.core.DomainException
import com.detrig.core.GenericDomainException
import com.detrig.core.NoInternetException
import com.detrig.core.model.FoundCity
import com.detrig.weather.data.mappers.toDomain
import com.detrig.weather.data.models.WeatherCloud
import com.detrig.weather.domain.WeatherRepository
import com.detrig.weather.domain.WeatherResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val cachedDataSource: WeatherCachedDataSource,
    private val weatherCloudDataSource: WeatherCloudDataSource,
) : WeatherRepository {

    private val gson = Gson()

    override suspend fun weather(): WeatherResult {
        val foundCity = cachedDataSource.getCityParams()
        try {
            val result =
                weatherCloudDataSource.getWeather(foundCity.latitude, foundCity.longitude)

            cachedDataSource.saveWeather(result, result.currentWeatherCloud.lastUpdatedTime)
            Log.d("alz-04", "weather result success: ${result.location.localTime}")
            return WeatherResult.Base(
                result.toDomain(foundCity.name)
            )
        } catch (e: DomainException) {
            Log.d("alz-04", "weather error: ${e}")
            val cachedWeather = cachedDataSource.getCachedWeather()
            return if (cachedWeather != null) {
                val cloud = gson.fromJson(cachedWeather.json, WeatherCloud::class.java)
                if (cloud.location.cityName == foundCity.name)
                    WeatherResult.Base(cloud.toDomain(foundCity.name))
                else WeatherResult.Failed(NoInternetException)
            } else {
                WeatherResult.Failed(e)
            }
        }
    }

    override suspend fun getSavedCity(): FoundCity {
        return cachedDataSource.getCityParams()
    }

    override fun observeWeather(): Flow<WeatherResult> {
        return cachedDataSource.observeCachedWeather()
            .map { entity ->
                if (entity == null) {
                    WeatherResult.Empty
                } else {
                    val cloud = gson.fromJson(entity.json, WeatherCloud::class.java)
                    //val foundCity = cachedDataSource.getCityParams()
                    WeatherResult.Base(cloud.toDomain(cloud.location.cityName))
                }
            }
            .catch { e ->
                emit(WeatherResult.Failed(GenericDomainException(e)))
            }
    }
}