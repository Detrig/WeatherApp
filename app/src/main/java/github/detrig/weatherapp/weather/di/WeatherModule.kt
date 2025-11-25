package github.detrig.weatherapp.weather.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import github.detrig.weatherapp.weather.data.WeatherCachedDataSource
import github.detrig.weatherapp.weather.data.WeatherCloudDataSource
import github.detrig.weatherapp.weather.data.WeatherService
import github.detrig.weatherapp.weather.domain.WeatherRepository
import github.detrig.weatherapp.weather.domain.WeatherResult
import github.detrig.weatherapp.weather.presentation.WeatherScreenUiState
import github.detrig.weatherapp.weather.presentation.mappers.WeatherUiMapper
import github.detrig.weatherapp.weather.presentation.models.AirQualityUiMapper
import retrofit2.Retrofit
import javax.inject.Named

@Module
@InstallIn(ViewModelComponent::class)
class WeatherModule {

    @Provides
    fun provideWeatherService(@Named("weather") retrofit: Retrofit): WeatherService =
        retrofit.create(WeatherService::class.java)
}

@Module
@InstallIn(ViewModelComponent::class)
abstract class WeatherBindsModule {

    @Binds
    abstract fun bindWeatherCachedDataSource(weatherCachedDataSource: WeatherCachedDataSource.Base): WeatherCachedDataSource

    @Binds
    abstract fun bindWeatherCloudDataSource(weatherCloudDataSource: WeatherCloudDataSource.Base): WeatherCloudDataSource

    @Binds
    abstract fun bindWeatherRepository(repository: WeatherRepository.Base): WeatherRepository

    @Binds
    abstract fun bindWeatherUiMapper(mapper: WeatherUiMapper): WeatherResult.Mapper<WeatherScreenUiState>

    @Binds
    abstract fun bindAirQualityUiMapper(mapper: AirQualityUiMapper.Base): AirQualityUiMapper
}
