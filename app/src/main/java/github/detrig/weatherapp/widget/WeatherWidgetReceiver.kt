package github.detrig.weatherapp.widget

import android.content.Context
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.width
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import dagger.hilt.android.EntryPointAccessors
import github.detrig.weatherapp.core.WeatherParamsParser
import github.detrig.weatherapp.main.MainActivity
import github.detrig.weatherapp.weather.domain.WeatherResult
import github.detrig.weatherapp.weather.domain.models.Weather
import github.detrig.weatherapp.weather.workers.WeatherWorkerEntryPoint
import kotlinx.coroutines.flow.firstOrNull
import java.time.format.DateTimeFormatter

class WeatherWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = WeatherWidget()
}

class WeatherWidget : GlanceAppWidget() {

    override suspend fun provideGlance(
        context: Context,
        id: GlanceId
    ) {
        val entryPoint = EntryPointAccessors.fromApplication(
            context,
            WeatherWorkerEntryPoint::class.java
        )

        val weatherRepository = entryPoint.weatherRepository()

        val result = weatherRepository.observeWeather().firstOrNull()
        val weather: Weather? = (result as? WeatherResult.Base)?.weather

        provideContent {
            GlanceTheme {
                WidgetUiWrapper(weather)
            }
        }
    }

}

private val DATE_TIME_FORMATTER =
    DateTimeFormatter.ofPattern("HH:mm")

@Composable
fun WidgetUiWrapper(
    weather: Weather?
) {
    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .padding(12.dp)
            .background(WeatherParamsParser.backgroundColorForCondition(weather?.condition))
            .clickable(actionStartActivity<MainActivity>())
    ) {
        if (weather == null) {
            Text("Нет данных о погоде")
        } else {
            Text(text = weather.cityName, style = TextStyle(fontSize = 10.sp))
            Row {
                Column {
                    Text(text = "${weather.temperature.toInt()}°C")

                    Text(text = weather.condition, style = TextStyle(fontSize = 10.sp))
                    Text(
                        text = weather.localTime.format(DATE_TIME_FORMATTER),
                        style = TextStyle(fontSize = 10.sp)
                    )
                }

                Spacer(GlanceModifier.width(16.dp))
                Image(
                    provider = ImageProvider(
                        WeatherParamsParser.mapCurrentWeatherIcon(
                            weather.localTime,
                            weather.temperature,
                            weather.condition,
                            weather.windSpeed
                        )
                    ),
                    contentDescription = weather.condition,
                    modifier = GlanceModifier.size(32.dp)
                )
            }
        }
    }
}

