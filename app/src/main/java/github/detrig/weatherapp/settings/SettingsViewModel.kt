package github.detrig.weatherapp.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import github.detrig.weatherapp.weather.domain.notification.NotificationsPrefs
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val notificationsPrefs: NotificationsPrefs
) : ViewModel() {

    val notificationsEnabled =
        notificationsPrefs.notificationsEnabledFlow
            .stateIn(
                viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = false
            )

    fun enableNotifications() {
        viewModelScope.launch {
            notificationsPrefs.setNotificationsEnabled(true)
        }
    }

    fun disableNotifications() {
        viewModelScope.launch {
            notificationsPrefs.setNotificationsEnabled(false)
        }
    }

    fun onNotificationsPermissionGranted() {
        enableNotifications()
    }

    fun onNotificationsPermissionDenied() {
        disableNotifications()
    }
}
