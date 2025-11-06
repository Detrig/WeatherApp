package github.detrig.weatherapp.findcity.domain

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import github.detrig.weatherapp.R
import javax.inject.Inject
import androidx.core.content.edit

interface FindCityCachedDataSource {

    suspend fun save(cityName: String, country: String, latitude: Double, longitude: Double)

    class Base @Inject constructor(
        @ApplicationContext context: Context,
    ) : FindCityCachedDataSource {

        private val sharedPreferences =
            context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

        override suspend fun save(
            cityName: String,
            country: String,
            latitude: Double,
            longitude: Double
        ) {
            sharedPreferences.edit() {
                putString(NAME, cityName)
                    .putString(COUNTRY, country)
                    .putFloat(LATITUDE, latitude.toFloat())
                    .putFloat(LONGITUDE, longitude.toFloat())
            }
        }

        companion object {
            private const val NAME = "cityNameKey"
            private const val COUNTRY = "countryKey"
            private const val LATITUDE = "latitudeKey"
            private const val LONGITUDE = "longitudeKey"
        }
    }
}