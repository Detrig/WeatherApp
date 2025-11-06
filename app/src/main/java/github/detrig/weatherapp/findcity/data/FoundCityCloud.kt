package github.detrig.weatherapp.findcity.data

import com.google.gson.annotations.SerializedName

data class FoundCityCloud(
    @SerializedName("name")
    val name: String,

    @SerializedName("country")
    val country: String,

    @SerializedName("lat")
    val latitude: Double,

    @SerializedName("lon")
    val longitude: Double
)
