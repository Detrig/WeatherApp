package github.detrig.weatherapp.findcity.data

import android.content.Context
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import github.detrig.weatherapp.core.AbstractCachedDataSource
import javax.inject.Inject

interface FindCityCachedDataSource {

    suspend fun save(cityName: String, country: String, latitude: Float, longitude: Float)

    class Base @Inject constructor(
        @ApplicationContext context: Context,
    ) : FindCityCachedDataSource, AbstractCachedDataSource(context) {

        override suspend fun save(
            cityName: String,
            country: String,
            latitude: Float,
            longitude: Float
        ) {
            sharedPreferences.edit() {
                putString(NAME, cityName)
                    .putString(COUNTRY, country)
                    .putFloat(LATITUDE, latitude.toFloat())
                    .putFloat(LONGITUDE, longitude.toFloat())
            }
        }
    }
}