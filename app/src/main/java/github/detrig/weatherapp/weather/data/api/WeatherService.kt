package github.detrig.weatherapp.weather.data.api

import github.detrig.weatherapp.weather.data.models.WeatherCloud
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    /**
     * weather
    https://api.weatherapi.com/v1/current.json?key=7f63ebcffd214161b8794516250611&q=Moscow&aqi=yes
    https://api.weatherapi.com/v1/current.json?key=7f63ebcffd214161b8794516250611&q=48.854,2.3508&aqi=yes
     **/

    @GET("forecast.json")
    suspend fun getWeather(
        @Query("key") apiKey: String,
        @Query("q") query: String, //"${lat},${lon}"
        @Query("days") days: Int = 3,
        @Query("aqi") airQualityFlag: String = "yes"
    ): WeatherCloud
}