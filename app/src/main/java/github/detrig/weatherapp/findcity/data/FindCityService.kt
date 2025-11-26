package github.detrig.weatherapp.findcity.data

import github.detrig.weatherapp.findcity.data.models.FoundCityCloud
import retrofit2.http.GET
import retrofit2.http.Query

interface FindCityService {

    //API_KEY = 8508c6239a74b4c673310458615bf1c4
    /**
     *city
        https://api.weatherapi.com/v1/search.json?key=7f63ebcffd214161b8794516250611&q=Moscow
     */

    @GET("search.json")
    suspend fun findCity(
        @Query("key") apiKey: String,
        @Query("q") query: String
    ): List<FoundCityCloud>

}