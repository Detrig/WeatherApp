package github.detrig.weatherapp.weather.domain.notification

interface NotificationsPrefs {

    val notificationsEnabledFlow: kotlinx.coroutines.flow.Flow<Boolean>

    suspend fun setNotificationsEnabled(enabled: Boolean)

    suspend fun isNotificationsEnabled(): Boolean
}
