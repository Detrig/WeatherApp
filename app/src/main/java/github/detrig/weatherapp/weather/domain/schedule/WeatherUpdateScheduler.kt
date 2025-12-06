package github.detrig.weatherapp.weather.domain.schedule

interface WeatherUpdateScheduler {
    fun scheduleOneTimeDebugUpdate()
    fun schedulePeriodicUpdate()
}