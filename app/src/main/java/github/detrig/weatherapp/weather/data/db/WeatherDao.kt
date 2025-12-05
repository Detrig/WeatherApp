package github.detrig.weatherapp.weather.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {

    @Query("SELECT * FROM weather_cache LIMIT 1")
    fun observeCachedWeather(): Flow<WeatherCacheEntity?>

    @Query("SELECT * FROM weather_cache LIMIT 1")
    suspend fun getSavedWeather(): WeatherCacheEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(entity: WeatherCacheEntity)
}