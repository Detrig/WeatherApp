package github.detrig.weatherapp.weather.workers

import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import github.detrig.weatherapp.weather.domain.notification.WeatherNotificationSender
import github.detrig.weatherapp.weather.domain.WeatherRepository

@EntryPoint
@InstallIn(SingletonComponent::class)
interface WeatherWorkerEntryPoint {
    fun weatherRepository(): WeatherRepository
    fun weatherNotificationSender(): WeatherNotificationSender
}