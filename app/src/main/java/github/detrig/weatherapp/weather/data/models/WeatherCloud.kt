package github.detrig.weatherapp.weather.data.models

import com.google.gson.annotations.SerializedName

data class WeatherCloud(
    @SerializedName("location")
    val location: LocationCloud,

    @SerializedName("current")
    val currentWeatherCloud: CurrentWeatherCloud,

    @SerializedName("forecast")
    val forecast: Forecast
)

data class LocationCloud(
    @SerializedName("name")
    val cityName: String,

    @SerializedName("region")
    val region: String,

    @SerializedName("country")
    val county: String,

    @SerializedName("localtime")
    val localTime: String
)



