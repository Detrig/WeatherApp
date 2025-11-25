package github.detrig.weatherapp.findcity

import androidx.lifecycle.SavedStateHandle
import github.detrig.weatherapp.core.RunAsync
import github.detrig.weatherapp.findcity.domain.FindCityRepository
import github.detrig.weatherapp.findcity.domain.FindCityResult
import github.detrig.weatherapp.findcity.domain.models.FoundCity
import github.detrig.weatherapp.core.NoInternetException
import github.detrig.weatherapp.findcity.presentation.mappers.FindCityUiMapper
import github.detrig.weatherapp.findcity.presentation.FindCityViewModel
import github.detrig.weatherapp.findcity.presentation.FoundCityScreenUiState
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
    private lateinit var findCityMapper: FindCityResult.Mapper<FoundCityScreenUiState>
    private lateinit var viewModel: FindCityViewModel

    @Before
    fun setUp() {
        repository = FakeFindCityRepository()
        savedStateHandle = SavedStateHandle()
        runAsync = FakeRunAsync()
        findCityMapper = FindCityUiMapper()
        viewModel = FindCityViewModel(
            mapper = findCityMapper,
            savedStateHandle = savedStateHandle,
            repository = repository,
            runAsync = runAsync
        )
    }

    @Test
    fun errorThenFindCityThenSaveIt() {
        val state: StateFlow<FoundCityScreenUiState> = viewModel.state
        var actual: FoundCityScreenUiState = state.value
        assertEquals(FoundCityScreenUiState.Empty, actual)

        viewModel.findCity("")
        assertEquals(FoundCityScreenUiState.Empty, state.value)

        viewModel.findCity("FUCK")
        assertEquals(
            FoundCityScreenUiState.Loading,
            state.value
        ) //Сначала Empty тк результат приходит асинхронно
        runAsync.returnResult()
        assertEquals(FoundCityScreenUiState.NoConnectionError, state.value)

        viewModel.findCity("FUCK")
        assertEquals(FoundCityScreenUiState.Loading, state.value)
        runAsync.returnResult()
        assertEquals(FoundCityScreenUiState.Empty, state.value)

        viewModel.findCity(cityName = "Mos")
        assertEquals(FoundCityScreenUiState.Loading, state.value)

        runAsync.returnResult()
        val foundCityList = listOf(
            FoundCity(
                name = "Moscow",
                latitude = 55.75f,
                country = "Russia",
                longitude = 37.61f
            ), FoundCity(name = "Moscow", latitude = 55.75f, country = "USA", longitude = 37.61f)
        )
        assertEquals(
            FoundCityScreenUiState.Base(foundCityList), state.value
        )

        viewModel.saveChosenCity(foundCityList.first())
        repository.assertSaveCalled(foundCityList.first())
    }
}

private class FakeFindCityRepository : FindCityRepository {

    private var shouldShowError = true

    override suspend fun findCity(query: String): FindCityResult { //List<FoundCity>
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