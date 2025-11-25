package github.detrig.weatherapp.weather.domain

import github.detrig.weatherapp.findcity.domain.DomainException
import github.detrig.weatherapp.findcity.domain.NoInternetException
import java.io.Serializable

interface WeatherResult {

    fun <T : Serializable> map(mapper: Mapper<T>): T

    interface Mapper<T : Serializable> {

        fun mapWeatherInCity(weatherInCity: WeatherInCity): T

        fun mapEmpty(): T

        fun mapNoInternetError(): T

        fun mapGenericError(): T

        fun mapLoading(): T
    }

    data class Base(
        private val weatherInCity: WeatherInCity
    ) : WeatherResult {
        override fun <T : Serializable> map(mapper: Mapper<T>): T {
            return mapper.mapWeatherInCity(weatherInCity)
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