package com.detrig.weather.domain.widget


interface WeatherWidgetUpdater {
    suspend fun updateWidgets()
}