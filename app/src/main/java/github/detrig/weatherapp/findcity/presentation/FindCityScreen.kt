package github.detrig.weatherapp.findcity.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import github.detrig.weatherapp.R
import github.detrig.weatherapp.findcity.domain.FoundCity
import java.io.Serializable

@Composable
fun FindCityScreen(
    viewModel: FindCityViewModel,
    navigateToWeatherScreen: () -> Unit
) {
    val input = rememberSaveable { mutableStateOf("") }
    val foundCityUi = viewModel.state.collectAsStateWithLifecycle()

    FindCityScreenUi(
        input = input.value,
        onInputChange = { text ->
            if (text.isNotEmpty()) {
                viewModel.findCity(cityName = text)
                input.value = text
            }
        },
        foundCityUi = foundCityUi.value,
        onFoundCityClick = { foundCity: FoundCity ->
            viewModel.saveChosenCity(foundCity = foundCity)
            navigateToWeatherScreen.invoke()
        },
        onRetryClick = {
            viewModel.findCity(cityName = input.value)
        }
    )
}

@Composable
fun FindCityScreenUi(
    input: String,
    onInputChange: (String) -> Unit,
    foundCityUi: FoundCityUi,
    onFoundCityClick: (FoundCity) -> Unit,
    onRetryClick: () -> Unit
) {

    Column {
        OutlinedTextField(
            modifier = Modifier
                .testTag("findCityInputField")
                .fillMaxWidth()
                .padding(8.dp),
            label = { Text("City") },
            value = input,
            onValueChange = onInputChange,
        )
        foundCityUi.Show(onFoundCityClick, onRetryClick)
    }
}


interface FoundCityUi : Serializable {

    @Composable
    fun Show(onFoundCityClick: (FoundCity) -> Unit, onRetryClick: () -> Unit)

    data object Empty : FoundCityUi {
        private fun readResolve(): Any = Empty

        @Composable
        override fun Show(onFoundCityClick: (FoundCity) -> Unit, onRetryClick: () -> Unit) {

        }
    }

    data class Base(private val foundCityList: List<FoundCity>) : FoundCityUi {

        @Composable
        override fun Show(onFoundCityClick: (FoundCity) -> Unit, onRetryClick: () -> Unit) {
            //todo Button(onClick = onfoundCityClick.invoke(foundcity)

            LazyColumn(Modifier.testTag("foundСityListUi")) {
                items(foundCityList) {
                    CityListItem(onFoundCityClick, it)
                }
            }
        }
    }

    data object Loading : FoundCityUi {
        private fun readResolve(): Any = Loading

        @Composable
        override fun Show(
            onFoundCityClick: (FoundCity) -> Unit,
            onRetryClick: () -> Unit
        ) {
            LoadingUi()
        }

    }

    data object NoConnectionError : FoundCityUi {
        private fun readResolve(): Any = NoConnectionError

        @Composable
        override fun Show(
            onFoundCityClick: (FoundCity) -> Unit,
            onRetryClick: () -> Unit
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("noInternetConnectionLayer"),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.no_internet),
                    modifier = Modifier.size(128.dp),
                    contentDescription = stringResource(R.string.no_internet_connection)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = stringResource(R.string.no_internet_connection))
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = onRetryClick,
                    modifier = Modifier
                        .testTag("retryButton")
                        .width(128.dp)
                ) {
                    Text(text = stringResource(R.string.retry))
                }
            }
        }
    }
}

@Composable
fun LoadingUi() {
    Box(Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxWidth().testTag("CircleLoading"),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
        }
    }
}

@Composable
fun CityListItem(onFoundCityClick: (FoundCity) -> Unit, foundCity: FoundCity) {
    Button(
        onClick = {
            onFoundCityClick.invoke(foundCity)
        }, modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        Text(
            text = "${foundCity.name} - ${foundCity.country}"
        )
    }
}

@Preview(showBackground = true, name = "Empty")
@Composable
fun PreviewEmptyFindCityScreenUi() {
    FindCityScreenUi(
        input = "",
        onInputChange = {},
        foundCityUi = FoundCityUi.Empty,
        onFoundCityClick = {}) {

    }
}

@Preview(showBackground = true, name = "No internet connection")
@Composable
fun PreviewNoInternetConnectionFindCityScreenUi() {
    FindCityScreenUi(
        input = "mos",
        onInputChange = {},
        foundCityUi = FoundCityUi.NoConnectionError,
        onFoundCityClick = {}) {
    }
}


@Preview(showBackground = true, name = "Success")
@Composable
fun PreviewNotEmptyFindCityScreenUi() {
    FindCityScreenUi(
        input = "Mosc", onInputChange = {}, foundCityUi = FoundCityUi.Base(
            foundCityList = listOf(
                FoundCity(
                    name = "Moscow",
                    country = "Russia",
                    latitude = 55.75f,
                    longitude = 37.61f
                ),
                FoundCity(
                    name = "Moscow",
                    country = "USA",
                    latitude = 55.75f,
                    longitude = 37.61f
                ),
                FoundCity(
                    name = "Moscow",
                    country = "Russia",
                    latitude = 55.75f,
                    longitude = 37.61f
                ),
            )
        ), onFoundCityClick = {}, onRetryClick = { }
    )
}