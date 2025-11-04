package github.detrig.weatherapp.findcity.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
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
            viewModel.findCity(cityName = text)
            input.value = text
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
        BasicTextField(
            modifier = Modifier.testTag("findCityInputField"),
            value = input,
            onValueChange = onInputChange
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

    data class Base(private val foundCity: FoundCity) : FoundCityUi {

        @Composable
        override fun Show(onFoundCityClick: (FoundCity) -> Unit) {
            //todo Button(onClick = onfoundCityClick.invoke(foundcity)

            Button(
                onClick = {
                    onFoundCityClick.invoke(foundCity)
                }) {
                Text(text = foundCity.name, modifier = Modifier.testTag("foundCityUi"))
            }
        }
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
            foundCity = FoundCity(
                name = "Moscow",
                latitude = 55.75,
                longitude = 37.61
            )
        )
    ) {

    }
}