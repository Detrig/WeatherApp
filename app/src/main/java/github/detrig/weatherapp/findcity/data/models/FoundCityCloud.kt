package github.detrig.weatherapp.findcity.data.models

import com.google.gson.annotations.SerializedName

data class FoundCityCloud(
    @SerializedName("name")
    val name: String,

    @SerializedName("country")
    val country: String,

    @SerializedName("lat")
    val latitude: Float,

    @SerializedName("lon")
    val longitude: Float
)