package github.detrig.weatherapp.findcity.domain

import github.detrig.weatherapp.core.DomainException
import github.detrig.weatherapp.findcity.domain.models.FoundCity
import github.detrig.weatherapp.core.NoInternetException
import java.io.Serializable

interface FindCityResult {

    fun <T : Serializable> map(mapper: Mapper<T>): T

    interface Mapper<T : Serializable> {

        fun mapFoundCity(foundCity: List<FoundCity>): T

        fun mapEmpty(): T

        fun mapLoading(): T

        fun mapNoInternetError(): T

        fun mapGenericError(): T
    }

    data class Base(
        val foundCity: List<FoundCity>
    ) : FindCityResult {

        override fun <T : Serializable> map(mapper: Mapper<T>): T {
            return mapper.mapFoundCity(foundCity)
        }
    }

    data class Failed(private val error: DomainException) : FindCityResult {
        override fun <T : Serializable> map(mapper: Mapper<T>): T =
            when (error) {
                is NoInternetException -> mapper.mapNoInternetError()
                else -> mapper.mapGenericError()
            }
    }

    data object Empty : FindCityResult {
        override fun <T : Serializable> map(mapper: Mapper<T>): T {
            return mapper.mapEmpty()
        }
    }

    data object Loading : FindCityResult {
        override fun <T : Serializable> map(mapper: Mapper<T>): T {
            return mapper.mapLoading()
        }
    }
}