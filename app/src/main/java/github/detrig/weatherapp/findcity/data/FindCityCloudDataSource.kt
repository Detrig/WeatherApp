package github.detrig.weatherapp.findcity.data

import javax.inject.Inject

interface FindCityCloudDataSource {

    suspend fun findCity(query: String): List<FoundCityCloud>

    class Base @Inject constructor(
        private val service: FindCityService
    ) : FindCityCloudDataSource {

        override suspend fun findCity(query: String): List<FoundCityCloud> {
            return service.findCity("7f63ebcffd214161b8794516250611", query)
        }
    }
}