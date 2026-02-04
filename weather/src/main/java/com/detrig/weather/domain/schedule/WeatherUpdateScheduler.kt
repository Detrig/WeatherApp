package com.detrig.weather.domain.schedule

interface WeatherUpdateScheduler {
    fun scheduleOneTimeDebugUpdate()
    fun schedulePeriodicUpdate()
}