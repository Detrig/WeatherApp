package com.detrig.weather.domain

import com.detrig.core.model.FoundCity
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {

    suspend fun weather(): WeatherResult
    suspend fun getSavedCity(): FoundCity
    fun observeWeather(): Flow<WeatherResult>
}