package com.detrig.weather.data.shedule

import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.detrig.weather.domain.schedule.WeatherUpdateScheduler
import com.detrig.weather.workers.UpdateWeatherWorker
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
            PeriodicWorkRequestBuilder<UpdateWeatherWorker>(Duration.ofMinutes(30))
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