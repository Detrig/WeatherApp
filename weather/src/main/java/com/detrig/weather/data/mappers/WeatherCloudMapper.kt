package com.detrig.weather.data.mappers

import com.detrig.weather.data.models.WeatherCloud
import com.detrig.weather.domain.models.AirQuality
import com.detrig.weather.domain.models.Weather

fun WeatherCloud.toDomain(foundCityName: String): Weather {
    return Weather(
        localTime = this.location.localTime.toLocalTime(),
        cityName = foundCityName,
        temperature = this.currentWeatherCloud.temp,
        feelTemperature = this.currentWeatherCloud.feelTemp,
        windSpeed = this.currentWeatherCloud.windSpeed,
        uv = this.currentWeatherCloud.uv,
        condition = this.currentWeatherCloud.conditionCloud.text,
        airQuality = AirQuality(
            co = this.currentWeatherCloud.airQuality.co,
            no2 = this.currentWeatherCloud.airQuality.no2,
            o3 = this.currentWeatherCloud.airQuality.o3,
            so2 = this.currentWeatherCloud.airQuality.so2,
            pm25 = this.currentWeatherCloud.airQuality.pm25,
            pm10 = this.currentWeatherCloud.airQuality.pm10,
        ),
        forecastDay = this.forecast.forecastDay.map { it.toDomain() }
    )
}

