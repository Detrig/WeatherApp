package github.detrig.weatherapp.widget

import android.content.Context
import androidx.glance.appwidget.updateAll
import dagger.hilt.android.qualifiers.ApplicationContext
import github.detrig.weatherapp.weather.domain.widget.WeatherWidgetUpdater
import javax.inject.Inject

class WeatherWidgetUpdaterImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : WeatherWidgetUpdater {

    override suspend fun updateWidgets() {
        WeatherWidget().updateAll(context)
    }
}