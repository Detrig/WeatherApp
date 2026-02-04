package com.detrig.core.notification

import kotlinx.coroutines.flow.Flow

interface NotificationsPrefs {

    val notificationsEnabledFlow: Flow<Boolean>

    suspend fun setNotificationsEnabled(enabled: Boolean)

    suspend fun isNotificationsEnabled(): Boolean
}
