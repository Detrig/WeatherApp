package github.detrig.weatherapp.weather.data

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import github.detrig.weatherapp.core.AbstractCachedDataSource
import github.detrig.weatherapp.findcity.domain.models.FoundCity
import github.detrig.weatherapp.weather.data.db.WeatherCacheEntity
import github.detrig.weatherapp.weather.data.db.WeatherDao
import github.detrig.weatherapp.weather.data.models.WeatherCloud
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface WeatherCachedDataSource {

    fun getCityParams(): FoundCity
    suspend fun saveWeather(weatherCloud: WeatherCloud, updatedAt: Long)
    suspend fun getCachedWeather(): WeatherCacheEntity?
    fun observeCachedWeather(): Flow<WeatherCacheEntity?>

    class Base @Inject constructor(
        @ApplicationContext context: Context,
        private val weatherDao: WeatherDao,
    ) : WeatherCachedDataSource, AbstractCachedDataSource(context) {

        private val gson = Gson()

        override fun getCityParams(): FoundCity {
            val cityName = sharedPreferences.getString(NAME, "") ?: ""
            val country = sharedPreferences.getString(COUNTRY, "") ?: ""
            val latitude = sharedPreferences.getFloat(LATITUDE, 0f)
            val longitude = sharedPreferences.getFloat(LONGITUDE, 0f)

            return FoundCity(
                name = cityName,
                country = country,
                latitude = latitude,
                longitude = longitude
            )
        }

        override suspend fun saveWeather(weatherCloud: WeatherCloud, updatedAt: Long) {
            val json = gson.toJson(weatherCloud)
            Log.d("alz-04", "saveWeather: ${weatherCloud.location.localTime}")
            weatherDao.insertWeather(
                WeatherCacheEntity(
                    json = json,
                    updatedAt = updatedAt
                )
            )
        }

        override suspend fun getCachedWeather(): WeatherCacheEntity? =
            weatherDao.getSavedWeather()

        override fun observeCachedWeather(): Flow<WeatherCacheEntity?> =
            weatherDao.observeCachedWeather()

    }
}