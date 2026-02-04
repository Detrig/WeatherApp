package com.detrig.weather.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.hilt.android.EntryPointAccessors
import com.detrig.weather.domain.notification.WeatherNotificationSender
import com.detrig.weather.domain.WeatherRepository
import com.detrig.weather.domain.WeatherResult
import com.detrig.weather.domain.models.Weather
import com.detrig.weather.domain.widget.WeatherWidgetUpdater
import kotlin.coroutines.cancellation.CancellationException

class UpdateWeatherWorker(
    context: Context,
    params: WorkerParameters,
) : CoroutineWorker(context, params) {

    private val entryPoint by lazy {
        EntryPointAccessors.fromApplication(
            applicationContext,
            WeatherWorkerEntryPoint::class.java
        )
    }

    private val weatherRepository: WeatherRepository by lazy {
        entryPoint.weatherRepository()
    }

    private val weatherNotificationSender: WeatherNotificationSender by lazy {
        entryPoint.weatherNotificationSender()
    }

    private val weatherWidgetUpdater: WeatherWidgetUpdater by lazy {
        entryPoint.weatherWidgetUpdater()
    }

    override suspend fun doWork(): Result {
        return try {
            val result = weatherRepository.weather()

            if (result is WeatherResult.Base) {
                val weather = result.weather
                if (shouldNotify(weather)) {
                    weatherNotificationSender.showWeatherUpdate(weather)
                }
            }

            weatherWidgetUpdater.updateWidgets()
            Log.d("alz-04", "UpdateWeatherWorker: YES")
            Result.success()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e("alz-04", "UpdateWeatherWorker: Error syncing data", e)
            Result.failure()
        }
    }

    private fun shouldNotify(weather: Weather): Boolean {
        // сюда можно вложить доменную логику:
        // - если пошёл дождь
        // - если ожидается снег
        // - если температура сильно изменилась и т.п
        return true
    }

}