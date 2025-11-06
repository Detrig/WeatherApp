package github.detrig.weatherapp.findcity.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import github.detrig.weatherapp.findcity.data.FindCityCloudDataSource
import github.detrig.weatherapp.findcity.data.FindCityService
import github.detrig.weatherapp.findcity.data.FindCityCachedDataSource
import github.detrig.weatherapp.findcity.domain.FindCityRepository
import retrofit2.Retrofit
import javax.inject.Named

@Module
@InstallIn(ViewModelComponent::class)
class FindCityModule {

    @Provides
    fun provideFindCityService(@Named("weather") retrofit: Retrofit): FindCityService =
        retrofit.create(FindCityService::class.java)
}

@Module
@InstallIn(ViewModelComponent::class)
abstract class FindCityBindsModule {

    @Binds
    abstract fun bindFindCityCloudDataSource(dataSource: FindCityCloudDataSource.Base): FindCityCloudDataSource

    @Binds
    abstract fun bindFindCityCachedDataSource(dataSource: FindCityCachedDataSource.Base): FindCityCachedDataSource

    @Binds
    abstract fun bindFindCityRepository(repository: FindCityRepository.Base): FindCityRepository
}