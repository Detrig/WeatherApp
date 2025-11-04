package github.detrig.weatherapp.findcity.domain

interface FindCityRepository {

    suspend fun findCity(query: String): FoundCity
    suspend fun saveCity(foundCity: FoundCity)
}