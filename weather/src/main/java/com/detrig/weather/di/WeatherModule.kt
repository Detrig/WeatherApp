package com.detrig.weather.di

import com.detrig.weather.data.service.WeatherNotificationSenderImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import com.detrig.weather.data.WeatherCachedDataSource
import com.detrig.weather.data.WeatherCloudDataSource
import com.detrig.weather.data.WeatherRepositoryImpl
import com.detrig.weather.data.api.WeatherService
import com.detrig.weather.data.db.CacheModule
import com.detrig.weather.data.db.WeatherDao
import com.detrig.weather.data.shedule.WeatherUpdateSchedulerImpl
import com.detrig.weather.domain.WeatherRepository
import com.detrig.weather.domain.WeatherResult
import com.detrig.weather.domain.notification.WeatherNotificationSender
import com.detrig.weather.domain.schedule.WeatherUpdateScheduler
import com.detrig.weather.presentation.WeatherScreenUiState
import com.detrig.weather.presentation.mappers.WeatherUiMapper
import com.detrig.weather.presentation.mappers.AirQualityUiMapper
import com.detrig.weather.presentation.mappers.ForecastDayUiMapper
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

    @Binds
    @Singleton
    abstract fun bindWeatherNotificationSender(
        impl: WeatherNotificationSenderImpl
    ): WeatherNotificationSender
}