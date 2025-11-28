package github.detrig.weatherapp.weather.data.models

import com.google.gson.annotations.SerializedName

data class Forecast(
    @SerializedName("forecastday")
    val forecastDay: List<ForecastDayCloud>
)

data class ForecastDayCloud(
    @SerializedName("date")
    val date: String,

    @SerializedName("day")
    val mainInfoForDay: WeatherDayCloud,

    @SerializedName("hour")
    val weatherForHour: List<WeatherForHourCloud>
)

data class WeatherDayCloud(
    @SerializedName("maxtemp_c")
    val maxTemp: Float,

    @SerializedName("mintemp_c")
    val minTemp: Float,

    @SerializedName("avgtemp_c")
    val avgTemp: Float,

    @SerializedName("maxwind_kph")
    val maxWind: Float,

    @SerializedName("daily_chance_of_rain")
    val dailyChanceOfRain: Int,

    @SerializedName("daily_chance_of_snow")
    val dailyChanceOfSnow: Int,

    @SerializedName("uv")
    val uv: Float,
)


data class WeatherForHourCloud(
    @SerializedName("time")
    val time: String, //"2025-11-27 00:00"

    @SerializedName("temp_c")
    val temp: Float,

    @SerializedName("is_day")
    val isDay: Int,

    @SerializedName("wind_kph")
    val windSpeed: Float,

    @SerializedName("uv")
    val uv: Float,

    @SerializedName("feelslike_c")
    val feelTemp: Float,

    @SerializedName("chance_of_rain")
    val chanceOfRain: Int,

    @SerializedName("chance_of_snow")
    val chanceOfSnow: Int,

    @SerializedName("cloud")
    val cloud: Int
)

