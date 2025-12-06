package github.detrig.weatherapp.widget.di

import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import github.detrig.weatherapp.weather.domain.WeatherRepository

@EntryPoint
@InstallIn(SingletonComponent::class)
interface WeatherWidgetEntryPoint {
    fun weatherRepository(): WeatherRepository
}