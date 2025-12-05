package github.detrig.weatherapp.weather.domain

interface WeatherUpdateScheduler {
    fun scheduleOneTimeDebugUpdate()
    fun schedulePeriodicUpdate()
}