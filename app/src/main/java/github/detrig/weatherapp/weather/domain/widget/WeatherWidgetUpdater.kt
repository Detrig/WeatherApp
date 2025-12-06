package github.detrig.weatherapp.weather.domain.widget


interface WeatherWidgetUpdater {
    suspend fun updateWidgets()
}