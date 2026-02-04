package com.detrig.weather.domain.notification

import com.detrig.weather.domain.models.Weather

interface WeatherNotificationSender {
    fun showWeatherUpdate(weather: Weather)
}