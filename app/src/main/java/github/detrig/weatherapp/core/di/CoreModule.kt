package github.detrig.weatherapp.core.di

import android.content.Context
import androidx.work.WorkManager
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import github.detrig.weatherapp.core.RunAsync
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CoreBindsModule {

    @Binds
    @Singleton
    abstract fun bindRunAsync(runAsync: RunAsync.Base): RunAsync
}

@Module
@InstallIn(SingletonComponent::class)
class CoreModule {
    //Internet
    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = Level.BODY//TODO change to Level.NONE
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    @Named("weather")
    fun provideWeatherRetrofit(client: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl("https://api.weatherapi.com/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    //WorkManager
    @Provides
    @Singleton
    fun provideWorkManager(@ApplicationContext context: Context): WorkManager =
        WorkManager.getInstance(context)
}