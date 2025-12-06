package github.detrig.weatherapp.core.notification

import github.detrig.weatherapp.weather.domain.notification.NotificationsPrefs

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.notificationsDataStore by preferencesDataStore(
    name = "notifications_prefs"
)

@Singleton
class NotificationsPrefsImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : NotificationsPrefs {

    private val dataStore = context.notificationsDataStore

    private val KEY_NOTIFICATIONS_ENABLED = booleanPreferencesKey("notifications_enabled")

    override val notificationsEnabledFlow: Flow<Boolean> =
        dataStore.data.map { prefs ->
            prefs[KEY_NOTIFICATIONS_ENABLED] ?: false
        }

    override suspend fun setNotificationsEnabled(enabled: Boolean) {
        dataStore.edit { prefs ->
            prefs[KEY_NOTIFICATIONS_ENABLED] = enabled
        }
    }

    override suspend fun isNotificationsEnabled(): Boolean {
        return dataStore.data
            .map { prefs -> prefs[KEY_NOTIFICATIONS_ENABLED] ?: false }
            .first()
    }
}
