package github.detrig.weatherapp.core.notification

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import github.detrig.weatherapp.R
import github.detrig.weatherapp.main.MainActivity
import github.detrig.weatherapp.weather.domain.notification.WeatherNotificationSender
import github.detrig.weatherapp.weather.domain.models.Weather
import github.detrig.weatherapp.weather.domain.notification.NotificationsPrefs
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherNotificationSenderImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val prefs: NotificationsPrefs
) : WeatherNotificationSender {

    private val notificationManager = NotificationManagerCompat.from(context)

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override fun showWeatherUpdate(weather: Weather) {
        val enabledByUser = runBlocking {
            prefs.isNotificationsEnabled()
        }
        if (!enabledByUser) {
            Log.d("alz-04", "Notifications disabled by user, skip")
            return
        }

        // 1. Создаём канал (один раз, но можем вызывать каждый раз — он idempotent)
        ensureChannel()

        // 2. PendingIntent для открытия приложения/экрана погоды
        val intent = Intent(context, MainActivity::class.java).apply {
            //Intent.setFlags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // 3. Текст уведомления
        val title = "Обновление погоды"
        val text = "Сейчас ${weather.temperature}°C, ${weather.condition}"

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.rainy80) // твой значок
            .setContentTitle(title)
            .setContentText(text)
            .setStyle(NotificationCompat.BigTextStyle().bigText(text))
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()


        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    private fun ensureChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Обновления погоды",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Уведомления о новых погодных данных"
            }
            val nm = context.getSystemService(NotificationManager::class.java)
            nm.createNotificationChannel(channel)
        }
    }

    companion object {
        private const val CHANNEL_ID = "weather_updates"
        private const val NOTIFICATION_ID = 1001
    }
}
