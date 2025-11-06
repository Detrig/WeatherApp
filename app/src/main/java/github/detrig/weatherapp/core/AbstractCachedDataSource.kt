package github.detrig.weatherapp.core

import android.content.Context
import android.content.SharedPreferences
import github.detrig.weatherapp.R

abstract class AbstractCachedDataSource(context: Context) {

    val sharedPreferences : SharedPreferences =
        context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

    companion object {
        const val NAME = "cityNameKey"
        const val COUNTRY = "countryKey"
        const val LATITUDE = "latitudeKey"
        const val LONGITUDE = "longitudeKey"
    }
}