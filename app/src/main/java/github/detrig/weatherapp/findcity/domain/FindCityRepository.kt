package github.detrig.weatherapp.findcity.domain

import android.util.Log
import github.detrig.weatherapp.findcity.data.FindCityCachedDataSource
import github.detrig.weatherapp.findcity.data.FindCityCloudDataSource
import github.detrig.weatherapp.core.DomainException
import github.detrig.weatherapp.findcity.domain.models.FoundCity
import javax.inject.Inject

interface FindCityRepository {

    suspend fun findCity(query: String): FindCityResult
    suspend fun saveCity(foundCity: FoundCity)

    class Base @Inject constructor(
        private val cloudDataSource: FindCityCloudDataSource,
        private val cachedDataSource: FindCityCachedDataSource
    ) : FindCityRepository {

        //Ловит высокоуровневые ошибки и оборачивает в Result
        override suspend fun findCity(query: String): FindCityResult {
            Log.d("alz-04", "query: $query")
            try {
                val foundCityCloud = cloudDataSource.findCity(query)
                if (foundCityCloud.isEmpty()) return FindCityResult.Empty
                val foundCityList = foundCityCloud.map {
                    FoundCity(
                        name = it.name,
                        country = it.country,
                        latitude = it.latitude,
                        longitude = it.longitude
                    )
                }
                return FindCityResult.Base(foundCityList)
            } catch (e: DomainException) {
                return FindCityResult.Failed(e)
            }
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