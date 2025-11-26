package github.detrig.weatherapp.weather.data.mappers

import github.detrig.weatherapp.weather.data.models.WeatherCloud
import github.detrig.weatherapp.weather.domain.models.AirQuality
import github.detrig.weatherapp.weather.domain.models.WeatherInCity

fun WeatherCloud.toDomain(foundCityName: String) : WeatherInCity {
    return WeatherInCity(
        cityName = foundCityName,
        temperature = this.currentWeather.temp,
        feelTemperature = this.currentWeather.feelTemp,
        windSpeed = this.currentWeather.windSpeed,
        uv = this.currentWeather.uv,
        condition = this.currentWeather.condition.text,
        airQuality = AirQuality(
            co = this.currentWeather.airQuality.co,
            no2 = this.currentWeather.airQuality.no2,
            o3 = this.currentWeather.airQuality.o3,
            so2 = this.currentWeather.airQuality.so2,
            pm25 = this.currentWeather.airQuality.pm25,
            pm10 = this.currentWeather.airQuality.pm10,
        )
    )
}
