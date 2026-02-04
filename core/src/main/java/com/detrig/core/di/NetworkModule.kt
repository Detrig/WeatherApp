package com.detrig.core.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.detrig.core.network.NetworkStatusRepository
import com.detrig.core.network.NetworkStatusRepositoryImpl
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