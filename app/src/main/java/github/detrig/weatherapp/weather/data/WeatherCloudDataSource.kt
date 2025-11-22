package github.detrig.weatherapp.weather.data

import github.detrig.weatherapp.findcity.domain.NoInternetException
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
            } catch (e: Exception) {
                if (e is IOException)
                    throw NoInternetException
                //todo else throw generic error
                throw e
            }
        }
    }
}

const val API_KEY = "7f63ebcffd214161b8794516250611"