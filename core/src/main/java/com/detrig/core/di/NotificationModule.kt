package com.detrig.core.di

import com.detrig.core.notification.NotificationsPrefs
import com.detrig.core.notification.NotificationsPrefsImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NotificationModule {

    @Binds
    @Singleton
    abstract fun bindNotificationsPrefs(
        impl: NotificationsPrefsImpl
    ): NotificationsPrefs
}