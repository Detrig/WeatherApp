package github.detrig.weatherapp

import androidx.test.espresso.core.internal.deps.dagger.Binds
import androidx.test.espresso.core.internal.deps.dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import github.detrig.weatherapp.core.NoInternetException
import github.detrig.weatherapp.core.RunAsync
import github.detrig.weatherapp.core.network.NetworkStatus
import github.detrig.weatherapp.core.network.NetworkStatusRepository
import github.detrig.weatherapp.findcity.di.FindCityBindsModule
import github.detrig.weatherapp.findcity.domain.FindCityRepository
import github.detrig.weatherapp.findcity.domain.FindCityResult
import github.detrig.weatherapp.findcity.domain.models.FoundCity
import github.detrig.weatherapp.weather.di.WeatherSingletonBindsModule
import github.detrig.weatherapp.weather.domain.WeatherRepository
import github.detrig.weatherapp.weather.domain.WeatherResult
import github.detrig.weatherapp.weather.domain.schedule.WeatherUpdateScheduler
import github.detrig.weatherapp.weather.domain.widget.WeatherWidgetUpdater
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import org.mockito.Mockito.mock
import javax.inject.Inject
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [WeatherSingletonBindsModule::class] // заменяем реальные репозитории и schedulers
)
abstract class TestWeatherSingletonBindsModule {

    @Singleton
    @Binds
    abstract fun bindWeatherRepository(repository: FakeWeatherRepository): WeatherRepository

    @Singleton
    @Binds
    abstract fun bindNetworkStatusRepository(repo: FakeNetworkStatusRepository): NetworkStatusRepository

    companion object {

        @Provides
        @Singleton
        fun provideWeatherUpdateScheduler(): WeatherUpdateScheduler =
            mock(WeatherUpdateScheduler::class.java)

        @Provides
        @Singleton
        fun provideWeatherWidgetUpdater(): WeatherWidgetUpdater =
            mock(WeatherWidgetUpdater::class.java)
    }
}


@Module
@TestInstallIn(
    components = [ViewModelComponent::class],
    replaces = [FindCityBindsModule::class] // заменяем основной биндинг модуль
)

abstract class TestFindCityBindsModule {

    @Binds
    abstract fun bindFindCityRepository(repository: FakeFindCityRepository): FindCityRepository

    // Добавьте другие биндинги, если ваши ViewModel используют:
    // FindCityCloudDataSource, FindCityCachedDataSource, FindCityUiMapper
}


@Singleton
class FakeFindCityRepository @Inject constructor() : FindCityRepository {

    private var shouldShowError = true

    override suspend fun findCity(query: String): FindCityResult {
        if (query.trim().isEmpty())
            throw IllegalStateException("repository should not accept empty query")

        if (query == "FUCK" && shouldShowError) {
            shouldShowError = false
            return FindCityResult.Failed(error = NoInternetException)
        } else if (query == "FUCK" && !shouldShowError) {
            return FindCityResult.Empty
        }

        if (query == "Mos")
            return FindCityResult.Base(
                listOf(
                    FoundCity(
                        name = "Moscow",
                        latitude = 55.75f,
                        country = "Russia",
                        longitude = 37.61f
                    ),
                    FoundCity(
                        name = "Moscow",
                        country = "USA",
                        latitude = 55.75f,
                        longitude = 37.61f
                    )
                )
            )

        throw IllegalStateException("not supported for this test")
    }

    override suspend fun saveCity(foundCity: FoundCity) {
        if (foundCity != FoundCity(
                name = "Moscow",
                country = "Russia",
                latitude = 55.75f,
                longitude = 37.61f
            )
        )
            throw IllegalStateException("save called with wrong argument $foundCity")
    }
}

@Singleton
class FakeWeatherRepository @Inject constructor() : WeatherRepository {

    private val weatherFlow = MutableStateFlow<WeatherResult>(WeatherResult.Empty)

    private var savedCity: FoundCity? = null

    suspend fun emit(result: WeatherResult) {
        weatherFlow.emit(result)
    }

    fun setSavedCity(city: FoundCity) {
        this.savedCity = city
    }


    override fun observeWeather(): Flow<WeatherResult> {
        return weatherFlow
    }

    override suspend fun weather(): WeatherResult {
        return weatherFlow.value
    }

    override suspend fun getSavedCity(): FoundCity {
        return savedCity
            ?: throw IllegalStateException("Saved city was not set in FakeWeatherRepository for this test. Call setSavedCity() first.")
    }
}

class FakeRunAsync : RunAsync {

    private var backgroundCached: (suspend () -> Any)? = null
    private var uiCached: ((Any) -> Unit)? = null

    override fun <T : Any> runAsync(
        scope: CoroutineScope,
        background: suspend () -> T,
        ui: (T) -> Unit
    ) {
        backgroundCached = background
        uiCached = { any -> ui(any as T) }
    }

    suspend fun returnResult() {
        val bg = backgroundCached ?: error("Background not set")
        val ui = uiCached ?: error("Ui not set")
        val result = bg()
        ui(result)
    }
}

class FakeNetworkStatusRepository @Inject constructor() : NetworkStatusRepository {

    private val statusFlow = MutableStateFlow<NetworkStatus>(NetworkStatus.Available)

    fun setNetworkStatus(newStatus: NetworkStatus) {
        statusFlow.value = newStatus
    }

    override fun observeNetworkStatus(): Flow<NetworkStatus> {
        return statusFlow
    }
}