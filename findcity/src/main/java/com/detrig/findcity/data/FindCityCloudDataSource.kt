package com.detrig.findcity.data

import com.detrig.findcity.data.models.FoundCityCloud
import com.detrig.core.GenericDomainException
import com.detrig.core.NoInternetException
import java.io.IOException
import javax.inject.Inject


//Cloudatasoruce ловит низкоуровневые ошибки, выдает высокоуровневые(NoInternetException)
interface FindCityCloudDataSource {

    suspend fun findCity(query: String): List<FoundCityCloud>

    class Base @Inject constructor(
        private val service: FindCityService
    ) : FindCityCloudDataSource {

        override suspend fun findCity(query: String): List<FoundCityCloud> {
            try {
                return service.findCity(API_KEY, query)
            } catch (e: IOException) {
                throw NoInternetException
            } catch (e: Exception) {
                throw GenericDomainException(e)
            }
        }
    }
}

const val API_KEY = "7f63ebcffd214161b8794516250611"