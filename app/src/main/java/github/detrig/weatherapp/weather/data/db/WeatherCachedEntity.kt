package github.detrig.weatherapp.weather.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather_cache")
data class WeatherCacheEntity(
    @PrimaryKey val id: Int = 0,
    val json: String,
    val updatedAt: Long,
)
