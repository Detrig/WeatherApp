package github.detrig.weatherapp.weather.domain

import github.detrig.weatherapp.findcity.domain.models.FoundCity
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {

    suspend fun weather(): WeatherResult
    suspend fun getSavedCity(): FoundCity
    fun observeWeather(): Flow<WeatherResult>
}