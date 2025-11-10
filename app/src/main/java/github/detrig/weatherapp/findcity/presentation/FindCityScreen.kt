package github.detrig.weatherapp.findcity.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
        }
    )
}

@Composable
fun FindCityScreenUi(
    input: String,
    onInputChange: (String) -> Unit,
    foundCityUi: FoundCityUi,
    onFoundCityClick: (FoundCity) -> Unit
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
        foundCityUi.Show(onFoundCityClick)
    }
}


interface FoundCityUi : Serializable {

    @Composable
    fun Show(onFoundCityClick: (FoundCity) -> Unit)

    data object Empty : FoundCityUi {
        private fun readResolve(): Any = Empty

        @Composable
        override fun Show(onFoundCityClick: (FoundCity) -> Unit) {

        }
    }

    data class Base(private val foundCityList: List<FoundCity>) : FoundCityUi {

        @Composable
        override fun Show(onFoundCityClick: (FoundCity) -> Unit) {
            //todo Button(onClick = onfoundCityClick.invoke(foundcity)

            LazyColumn(Modifier.testTag("foundityListUi")) {
                items(foundCityList) {
                    CityListItem(onFoundCityClick, it)
                }
            }
        }
    }
}

@Composable
fun CityListItem(onFoundCityClick: (FoundCity) -> Unit, foundCity: FoundCity) {
    Button(
        onClick = {
            onFoundCityClick.invoke(foundCity)
        }, modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)
    ) {
        Text(
            text = "${foundCity.name} - ${foundCity.country}"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewEmptyFindCityScreenUi() {
    FindCityScreenUi(input = "", onInputChange = {}, foundCityUi = FoundCityUi.Empty) {

    }
}


@Preview(showBackground = true)
@Composable
fun PreviewNotEmptyFindCityScreenUi() {
    FindCityScreenUi(
        input = "Mosc", onInputChange = {}, foundCityUi = FoundCityUi.Base(
            foundCityList = listOf(
                FoundCity(
                    name = "Moscow",
                    country = "Russia",
                    latitude = 55.75,
                    longitude = 37.61
                ),
                FoundCity(
                    name = "Moscow",
                    country = "USA",
                    latitude = 55.75,
                    longitude = 37.61
                ),
                FoundCity(
                    name = "Moscow",
                    country = "Russia",
                    latitude = 55.75,
                    longitude = 37.61
                ),
            )
        )
    ) {

    }
}