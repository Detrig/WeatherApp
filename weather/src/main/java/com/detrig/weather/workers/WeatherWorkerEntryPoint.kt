package com.detrig.weather.workers

import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.detrig.weather.domain.notification.WeatherNotificationSender
import com.detrig.weather.domain.WeatherRepository
import com.detrig.weather.domain.widget.WeatherWidgetUpdater

@EntryPoint
@InstallIn(SingletonComponent::class)
interface WeatherWorkerEntryPoint {
    fun weatherRepository(): WeatherRepository
    fun weatherNotificationSender(): WeatherNotificationSender
    fun weatherWidgetUpdater(): WeatherWidgetUpdater
}