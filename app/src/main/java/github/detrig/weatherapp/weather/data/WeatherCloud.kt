package github.detrig.weatherapp.weather.data

import com.google.gson.annotations.SerializedName

data class WeatherCloud(
    @SerializedName("current")
    val currentWeather: CurrentWeather
)

data class CurrentWeather(
    @SerializedName("temp_c")
    val temp: Float,

    @SerializedName("is_day")
    val isDay: Int,

    @SerializedName("condition")
    val condition: Condition,

    @SerializedName("wind_kph")
    val windSpeed: Float,

    @SerializedName("uv")
    val uv: Float,

    @SerializedName("feelslike_c")
    val feelTemp: Float
)

data class Condition(
    @SerializedName("text")
    val text: String,

    @SerializedName("icon")
    val iconUrl: String,

    @SerializedName("code")
    val code: Int
)

