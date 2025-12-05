package github.detrig.weatherapp.weather.data.models

import com.google.gson.annotations.SerializedName

data class CurrentWeatherCloud(
    @SerializedName("last_updated_epoch")
    val lastUpdatedTime: Long,

    @SerializedName("temp_c")
    val temp: Float,

    @SerializedName("is_day")
    val isDay: Int,

    @SerializedName("condition")
    val conditionCloud: ConditionCloud,

    @SerializedName("wind_kph")
    val windSpeed: Float,

    @SerializedName("uv")
    val uv: Float,

    @SerializedName("feelslike_c")
    val feelTemp: Float,

    @SerializedName("air_quality")
    val airQuality: AirQualityCloud
)

data class ConditionCloud(
    @SerializedName("text")
    val text: String,

    @SerializedName("icon")
    val iconUrl: String,

    @SerializedName("code")
    val code: Int
)
