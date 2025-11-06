package github.detrig.weatherapp.findcity.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import github.detrig.weatherapp.findcity.data.FindCityCloudDataSource
import github.detrig.weatherapp.findcity.data.FindCityService
import github.detrig.weatherapp.findcity.domain.FindCityCachedDataSource
import github.detrig.weatherapp.findcity.domain.FindCityRepository
import jakarta.inject.Named
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(ViewModelComponent::class)
class FindCityModule {

    @Provides
    fun provideWeatherService(@Named("weather") retrofit: Retrofit): FindCityService =
        retrofit.create(FindCityService::class.java)

    @Provides
    @Named("weather")
    fun provideWeatherRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl("https://api.weatherapi.com/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

@Module
@InstallIn(ViewModelComponent::class)
abstract class FindCityBindModule {

    @Binds
    abstract fun bindFindCityCloudDataSource(dataSource: FindCityCloudDataSource.Base): FindCityCloudDataSource

    @Binds
    abstract fun bindFindCityCachedDataSource(dataSource: FindCityCachedDataSource.Base): FindCityCachedDataSource

    @Binds
    abstract fun bindFindCityRepository(repository: FindCityRepository.Base): FindCityRepository
}