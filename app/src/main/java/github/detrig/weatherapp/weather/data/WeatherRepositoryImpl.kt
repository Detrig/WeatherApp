package github.detrig.weatherapp.weather.data

import com.google.gson.Gson
import github.detrig.weatherapp.core.DomainException
import github.detrig.weatherapp.core.GenericDomainException
import github.detrig.weatherapp.findcity.domain.models.FoundCity
import github.detrig.weatherapp.weather.data.mappers.toDomain
import github.detrig.weatherapp.weather.data.models.WeatherCloud
import github.detrig.weatherapp.weather.domain.WeatherRepository
import github.detrig.weatherapp.weather.domain.WeatherResult
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

            return WeatherResult.Base(
                result.toDomain(foundCity.name)
            )
        } catch (e: DomainException) {
            val cachedWeather = cachedDataSource.getCachedWeather()
            return if (cachedWeather != null) {
                val cloud = gson.fromJson(cachedWeather.json, WeatherCloud::class.java)
                WeatherResult.Base(cloud.toDomain(foundCity.name))
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
                    val foundCity = cachedDataSource.getCityParams()
                    WeatherResult.Base(cloud.toDomain(foundCity.name))
                }
            }
            .catch { e ->
                emit(WeatherResult.Failed(GenericDomainException(e)))
            }
    }
}