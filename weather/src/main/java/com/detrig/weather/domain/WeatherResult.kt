package com.detrig.weather.domain

import com.detrig.core.DomainException
import com.detrig.core.NoInternetException
import com.detrig.weather.domain.models.Weather
import java.io.Serializable

interface WeatherResult {

    fun <T : Serializable> map(mapper: Mapper<T>): T

    interface Mapper<T : Serializable> {

        fun mapWeatherInCity(weather: Weather): T

        fun mapEmpty(): T

        fun mapNoInternetError(): T

        fun mapGenericError(): T

        fun mapLoading(): T
    }

    data class Base(
        val weather: Weather
    ) : WeatherResult {
        override fun <T : Serializable> map(mapper: Mapper<T>): T {
            return mapper.mapWeatherInCity(weather)
        }
    }

    data class Failed(
        private val error: DomainException
    ) : WeatherResult {
        override fun <T : Serializable> map(mapper: Mapper<T>): T =
            when (error) {
                is NoInternetException -> mapper.mapNoInternetError()
                else -> mapper.mapGenericError()
            }
    }

    data object Empty : WeatherResult {
        override fun <T : Serializable> map(mapper: Mapper<T>): T {
            return mapper.mapEmpty()
        }

    }
}