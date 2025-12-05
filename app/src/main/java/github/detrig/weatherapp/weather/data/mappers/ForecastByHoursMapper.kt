package github.detrig.weatherapp.weather.data.mappers

import github.detrig.weatherapp.weather.data.models.ForecastDayCloud
import github.detrig.weatherapp.weather.data.models.WeatherDayCloud
import github.detrig.weatherapp.weather.data.models.WeatherForHourCloud
import github.detrig.weatherapp.weather.domain.models.ForecastDay
import github.detrig.weatherapp.weather.domain.models.WeatherDay
import github.detrig.weatherapp.weather.domain.models.WeatherForHour
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

private val DATE_TIME_FORMATTER =
    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

fun String.toLocalTime(): LocalTime {
    return LocalDateTime
        .parse(this, DATE_TIME_FORMATTER)
        .toLocalTime()
}

fun ForecastDayCloud.toDomain(): ForecastDay {
    return ForecastDay(
        date = LocalDate.parse(this.date),
        //day = this.mainInfoForDay.toDomain(),
        weatherForHour = this.weatherForHour.map { it.toDomain() }
    )
}

fun WeatherForHourCloud.toDomain(): WeatherForHour {
    return WeatherForHour(
        time = this.time.toLocalTime(),
        temp = this.temp,
        windSpeed = this.windSpeed,
        chanceOfRain = (this.chanceOfRain / 100f),
        chanceOfSnow = (this.chanceOfSnow / 100f),
        cloud = (this.cloud / 100f)
    )
}

//fun WeatherDayCloud.toDomain(): WeatherDay {
//    return WeatherDay(
//        maxTemp = this.maxTemp,
//        minTemp = this.minTemp,
//        avgTemp = this.avgTemp,
//        maxWind = this.maxWind,
//        dailyChanceOfRain = (this.dailyChanceOfRain / 100).toFloat(),
//        dailyChanceOfSnow = (this.dailyChanceOfSnow / 100).toFloat(),
//        uv = this.uv
//    )
//}
