package com.detrig.core

import android.content.Context
import android.content.SharedPreferences

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