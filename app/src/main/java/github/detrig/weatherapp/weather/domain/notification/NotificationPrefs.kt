package github.detrig.weatherapp.weather.domain.notification

interface NotificationsPrefs {

    /** Поток для реактивного UI — для Switch на экране настроек. */
    val notificationsEnabledFlow: kotlinx.coroutines.flow.Flow<Boolean>

    /** Установить флаг "уведомления включены". */
    suspend fun setNotificationsEnabled(enabled: Boolean)

    /** Синхронный "get" в виде `suspend` — удобно использовать внутри корутин. */
    suspend fun isNotificationsEnabled(): Boolean
}
