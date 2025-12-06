package github.detrig.weatherapp.weather.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import github.detrig.weatherapp.weather.data.WeatherCachedDataSource
import github.detrig.weatherapp.weather.data.WeatherCloudDataSource
import github.detrig.weatherapp.weather.data.WeatherRepositoryImpl
import github.detrig.weatherapp.weather.data.api.WeatherService
import github.detrig.weatherapp.weather.data.db.CacheModule
import github.detrig.weatherapp.weather.data.db.WeatherDao
import github.detrig.weatherapp.core.shedule.WeatherUpdateSchedulerImpl
import github.detrig.weatherapp.weather.domain.WeatherRepository
import github.detrig.weatherapp.weather.domain.WeatherResult
import github.detrig.weatherapp.weather.domain.schedule.WeatherUpdateScheduler
import github.detrig.weatherapp.weather.presentation.WeatherScreenUiState
import github.detrig.weatherapp.weather.presentation.mappers.WeatherUiMapper
import github.detrig.weatherapp.weather.presentation.mappers.AirQualityUiMapper
import github.detrig.weatherapp.weather.presentation.mappers.ForecastDayUiMapper
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class WeatherModule {

    @Provides
    @Singleton
    fun provideWeatherService(@Named("weather") retrofit: Retrofit): WeatherService =
        retrofit.create(WeatherService::class.java)

    @Provides
    @Singleton
    fun provideWeatherDao(cacheModule: CacheModule): WeatherDao = cacheModule.dao()
}

@Module
@InstallIn(ViewModelComponent::class)
abstract class WeatherBindsModule {

    @Binds
    abstract fun bindWeatherUiMapper(mapper: WeatherUiMapper): WeatherResult.Mapper<WeatherScreenUiState>

    @Binds
    abstract fun bindAirQualityUiMapper(mapper: AirQualityUiMapper.Base): AirQualityUiMapper

    @Binds
    abstract fun bindForecastDayUiMapper(mapper: ForecastDayUiMapper.Base): ForecastDayUiMapper
}

@Module
@InstallIn(SingletonComponent::class)
abstract class WeatherSingletonBindsModule {
    @Singleton
    @Binds
    abstract fun bindCacheModule(cacheModule: CacheModule.Base): CacheModule

    @Binds
    @Singleton
    abstract fun bindWeatherSyncScheduler(
        impl: WeatherUpdateSchedulerImpl
    ): WeatherUpdateScheduler

    @Binds
    @Singleton
    abstract fun bindWeatherCachedDataSource(weatherCachedDataSource: WeatherCachedDataSource.Base): WeatherCachedDataSource

    @Binds
    @Singleton
    abstract fun bindWeatherCloudDataSource(weatherCloudDataSource: WeatherCloudDataSource.Base): WeatherCloudDataSource

    @Binds
    @Singleton
    abstract fun bindWeatherRepository(repository: WeatherRepositoryImpl): WeatherRepository
}