package github.detrig.weatherapp.widget.di

import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.detrig.weather.domain.WeatherRepository

@EntryPoint
@InstallIn(SingletonComponent::class)
interface WeatherWidgetEntryPoint {
    fun weatherRepository(): WeatherRepository
}