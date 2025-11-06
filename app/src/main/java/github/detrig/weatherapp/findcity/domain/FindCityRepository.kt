package github.detrig.weatherapp.findcity.domain

import github.detrig.weatherapp.findcity.data.FindCityCloudDataSource
import javax.inject.Inject

interface FindCityRepository {

    suspend fun findCity(query: String): List<FoundCity>
    suspend fun saveCity(foundCity: FoundCity)


    class Base @Inject constructor(
        private val cloudDataSource: FindCityCloudDataSource,
        private val cachedDataSource: FindCityCachedDataSource
    ) : FindCityRepository {

        override suspend fun findCity(query: String): List<FoundCity> {
            val foundCityCloud = cloudDataSource.findCity(query)
            val foundCityList = foundCityCloud.map {
                FoundCity(
                    name = it.name,
                    country = it.country,
                    latitude = it.latitude,
                    longitude = it.longitude
                )
            }
            return foundCityList
        }

        override suspend fun saveCity(selectedCity: FoundCity) {
            cachedDataSource.save(
                selectedCity.name,
                selectedCity.country,
                selectedCity.latitude,
                selectedCity.longitude
            )
        }

    }
}