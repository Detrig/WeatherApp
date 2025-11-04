package github.detrig.weatherapp.findcity

import androidx.lifecycle.SavedStateHandle
import github.detrig.weatherapp.core.RunAsync
import github.detrig.weatherapp.findcity.domain.FindCityRepository
import github.detrig.weatherapp.findcity.domain.FoundCity
import github.detrig.weatherapp.findcity.presentation.FindCityViewModel
import github.detrig.weatherapp.findcity.presentation.FoundCityUi
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class FindCityViewModelTest {

    private lateinit var repository: FakeFindCityRepository
    private lateinit var savedStateHandle: SavedStateHandle
    private lateinit var runAsync: FakeRunAsync
    private lateinit var viewModel: FindCityViewModel

    @Before
    fun setUp() {
        repository = FakeFindCityRepository()
        savedStateHandle = SavedStateHandle()
        runAsync = FakeRunAsync()
        viewModel = FindCityViewModel(
            savedStateHandle = savedStateHandle,
            repository = repository,
            runAsync = runAsync
        )
    }

    @Test
    fun findCityAndSaveIt() {
        val state: StateFlow<FoundCityUi> = viewModel.state
        var actual: FoundCityUi = state.value
        assertEquals(FoundCityUi.Empty, actual)

        viewModel.findCity(cityName = "Mos")
        assertEquals(FoundCityUi.Empty, actual)
        runAsync.returnResult()
        val foundCity = FoundCity(name = "Moscow", latitude = 55.75, longitude = 37.61)
        assertEquals(
            FoundCityUi.Base(foundCity), viewModel.state.value
        )

        viewModel.saveChosenCity(foundCity)
        repository.assertSaveCalled(foundCity)

    }
}

private class FakeFindCityRepository : FindCityRepository {

    override suspend fun findCity(query: String): FoundCity {
        if (query == "Mos")
            return FoundCity(
                name = "Moscow",
                latitude = 55.75,
                longitude = 37.61
            )
        throw IllegalStateException("not supported for this test")
    }

    override suspend fun saveCity(foundCity: FoundCity) {
        savedCity = foundCity
    }

    fun assertSaveCalled(expected: FoundCity) {
        assertEquals(expected, savedCity)
    }

    private lateinit var savedCity: FoundCity
}


class FakeRunAsync : RunAsync {

    private lateinit var resultCached: Any
    private lateinit var uiCached: (Any) -> Unit

    override fun <T : Any> runAsync(
        scope: CoroutineScope,
        background: suspend () -> T,
        ui: (T) -> Unit
    ) {
        runBlocking {
            val result: T = background.invoke()
            resultCached = result
            uiCached = ui as (Any) -> Unit
        }
    }

    fun returnResult() {
        uiCached.invoke(resultCached)
    }
}