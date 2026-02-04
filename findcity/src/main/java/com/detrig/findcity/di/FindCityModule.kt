package com.detrig.findcity.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import com.detrig.findcity.data.FindCityCloudDataSource
import com.detrig.findcity.data.FindCityService
import com.detrig.findcity.data.FindCityCachedDataSource
import com.detrig.findcity.domain.FindCityRepository
import com.detrig.findcity.domain.FindCityResult
import com.detrig.findcity.presentation.mappers.FindCityUiMapper
import com.detrig.findcity.presentation.FoundCityScreenUiState
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

    @Binds
    abstract fun bindFindCityUiMapper(mapper: FindCityUiMapper): FindCityResult.Mapper<FoundCityScreenUiState>
}