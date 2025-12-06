package github.detrig.weatherapp.core.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import github.detrig.weatherapp.core.notification.NotificationsPrefsImpl
import github.detrig.weatherapp.core.notification.WeatherNotificationSenderImpl
import github.detrig.weatherapp.weather.domain.notification.NotificationsPrefs
import github.detrig.weatherapp.weather.domain.notification.WeatherNotificationSender
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NotificationModule {

    @Binds
    @Singleton
    abstract fun bindWeatherNotificationSender(
        impl: WeatherNotificationSenderImpl
    ): WeatherNotificationSender

    @Binds
    @Singleton
    abstract fun bindNotificationsPrefs(
        impl: NotificationsPrefsImpl
    ): NotificationsPrefs
}