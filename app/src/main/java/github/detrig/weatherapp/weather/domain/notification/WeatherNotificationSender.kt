package github.detrig.weatherapp.weather.domain.notification

import github.detrig.weatherapp.weather.domain.models.Weather

interface WeatherNotificationSender {
    fun showWeatherUpdate(weather: Weather)
}