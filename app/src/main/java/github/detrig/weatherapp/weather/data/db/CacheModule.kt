package github.detrig.weatherapp.weather.data.db

import android.content.Context
import androidx.room.Room
import dagger.hilt.android.qualifiers.ApplicationContext
import github.detrig.weatherapp.R
import javax.inject.Inject

interface CacheModule {

    fun dao(): WeatherDao

    class Base @Inject constructor(@ApplicationContext applicationContext: Context) : CacheModule {

        private val database by lazy {
            Room.databaseBuilder(
                applicationContext,
                WeatherDatabase::class.java,
                applicationContext.getString(R.string.app_name)
            ).build()
        }

        override fun dao(): WeatherDao = database.dao()
    }
}