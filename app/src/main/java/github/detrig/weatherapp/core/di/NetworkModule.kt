package github.detrig.weatherapp.core.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import github.detrig.weatherapp.core.network.NetworkStatusRepository
import github.detrig.weatherapp.core.network.NetworkStatusRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkModule {

    @Binds
    @Singleton
    abstract fun bindNetworkStatusRepository(
        impl: NetworkStatusRepositoryImpl
    ): NetworkStatusRepository
}