package github.detrig.weatherapp.weather.data.shedule

import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import github.detrig.weatherapp.weather.domain.WeatherUpdateScheduler
import github.detrig.weatherapp.weather.workers.UpdateWeatherWorker
import java.time.Duration
import javax.inject.Inject

class WeatherUpdateSchedulerImpl @Inject constructor(
    private val workManager: WorkManager
) : WeatherUpdateScheduler {
    override fun schedulePeriodicUpdate() {
//        val weatherConstraints = Constraints.Builder()
//            .setRequiredNetworkType(NetworkType.CONNECTED)
//            .build()

        val updateWeatherRequest =
            PeriodicWorkRequestBuilder<UpdateWeatherWorker>(Duration.ofMinutes(15))
                .build()

        workManager.enqueueUniquePeriodicWork(
            UNIQUE_PERIODIC_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            updateWeatherRequest
        )
    }

    override fun scheduleOneTimeDebugUpdate() {
        val request = OneTimeWorkRequestBuilder<UpdateWeatherWorker>()
            .setInitialDelay(Duration.ofMinutes(5))
            .build()

        workManager.enqueueUniqueWork(
            "weather_debug_one_time",
            ExistingWorkPolicy.REPLACE,
            request
        )
    }

    companion object {
        private const val UNIQUE_PERIODIC_NAME = "weather_periodic"
    }
}