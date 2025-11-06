package github.detrig.weatherapp.findcity.domain

import java.io.Serializable

data class FoundCity(
    val name: String,
    val country: String,
    val latitude: Double,
    val longitude: Double
) : Serializable