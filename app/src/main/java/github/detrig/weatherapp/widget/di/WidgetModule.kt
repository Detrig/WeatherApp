package github.detrig.weatherapp.widget.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import github.detrig.weatherapp.weather.domain.widget.WeatherWidgetUpdater
import github.detrig.weatherapp.widget.WeatherWidgetUpdaterImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class WidgetModule {

    @Binds
    @Singleton
    abstract fun bindWeatherWidgetUpdater(
        impl: WeatherWidgetUpdaterImpl
    ): WeatherWidgetUpdater
}