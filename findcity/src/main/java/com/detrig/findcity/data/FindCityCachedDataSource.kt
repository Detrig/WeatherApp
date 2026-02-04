package com.detrig.findcity.data

import android.content.Context
import androidx.core.content.edit
import com.detrig.core.AbstractCachedDataSource
import dagger.hilt.android.qualifiers.ApplicationContext
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