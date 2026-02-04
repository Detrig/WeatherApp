package com.detrig.weather.data

import com.detrig.core.GenericDomainException
import com.detrig.core.NoInternetException
import com.detrig.weather.data.api.WeatherService
import com.detrig.weather.data.models.WeatherCloud
import java.io.IOException
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
            try {
                return service.getWeather(API_KEY, "$latitude,$longitude")
            } catch (e: IOException) {
                throw NoInternetException
            } catch (e: Exception) {
                throw GenericDomainException(e)
            }
        }
    }
}

const val API_KEY = "7f63ebcffd214161b8794516250611"