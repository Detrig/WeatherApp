package github.detrig.weatherapp.weather.data

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import github.detrig.weatherapp.core.AbstractCachedDataSource
import github.detrig.weatherapp.findcity.domain.FoundCity
import javax.inject.Inject

interface WeatherCachedDataSource {

    fun getCityParams(): FoundCity

    class Base @Inject constructor(
        @ApplicationContext context: Context
    ) : WeatherCachedDataSource, AbstractCachedDataSource(context) {
        override fun getCityParams(): FoundCity {
            val cityName = sharedPreferences.getString(NAME, "") ?: ""
            val country = sharedPreferences.getString(COUNTRY, "") ?: ""
            val latitude = sharedPreferences.getFloat(LATITUDE, 0f)
            val longitude = sharedPreferences.getFloat(LONGITUDE, 0f)

            return FoundCity(
                name = cityName,
                country = country,
                latitude = latitude,
                longitude = longitude
            )
        }
    }
}