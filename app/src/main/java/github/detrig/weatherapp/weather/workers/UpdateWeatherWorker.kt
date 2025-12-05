package github.detrig.weatherapp.weather.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.hilt.android.EntryPointAccessors
import github.detrig.weatherapp.weather.domain.WeatherRepository

class UpdateWeatherWorker(
    context: Context,
    params: WorkerParameters,
) : CoroutineWorker(context, params) {

    private val weatherRepository: WeatherRepository by lazy {
        val entryPoint = EntryPointAccessors.fromApplication(
            context,
            WeatherWorkerEntryPoint::class.java
        )
        entryPoint.weatherRepository()
    }

    override suspend fun doWork(): Result {
        try {
            Log.d("alz-04", "UpdateWeatherWorker: YES")
            weatherRepository.weather()
            Log.d("alz-04", "UpdateWeatherWorker: YES YES")
            return Result.success()
        } catch (e: Exception) {
            Log.d("alz-04", "UpdateWeatherWorker: Error syncing data")
            return Result.failure()
        }
    }

}