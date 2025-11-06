package github.detrig.weatherapp.weather.data

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import github.detrig.weatherapp.core.AbstractCachedDataSource
import javax.inject.Inject

interface WeatherCachedDataSource {

    fun getCityParams() : Triple<Float, Float, String>

    class Base @Inject constructor(
        @ApplicationContext context: Context
    ) : WeatherCachedDataSource, AbstractCachedDataSource(context) {
        override fun getCityParams(): Triple<Float, Float, String> {
            val cityName = sharedPreferences.getString(NAME, "") ?: ""
            //val country = sharedPreferences.getString(COUNTRY)
            val latitude = sharedPreferences.getFloat(LATITUDE, 0f)
            val longitude = sharedPreferences.getFloat(LONGITUDE, 0f)

            return Triple(latitude, longitude, cityName)
        }
    }
}