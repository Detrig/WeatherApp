package github.detrig.weatherapp.weather.data.models

import com.google.gson.annotations.SerializedName

data class AirQualityCloud(
    @SerializedName("co")
    val co: Float,

    @SerializedName("no2")
    val no2: Float,

    @SerializedName("o3")
    val o3: Float,

    @SerializedName("so2")
    val so2: Float,

    @SerializedName("pm25")
    val pm25: Float,

    @SerializedName("pm10")
    val pm10: Float
)