package github.detrig.weatherapp.findcity.presentation

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext

@Composable
fun FindCityOrGetLocationScreen(
    viewModel: FindCityViewModel,
    navigateToWeatherScreen: () -> Unit
) {
    val context = LocalContext.current
    val getLocation = rememberSaveable { mutableStateOf(false) }

    if (getLocation.value) {
        GetUserLocationScreenWrapper(
            onLocationProvided = { lat, lon ->
                viewModel.findCityByLocation(lat, lon) {
                    getLocation.value = false
                    navigateToWeatherScreen.invoke()
                }
            },
            onFailed = { msg ->
                Toast.makeText(context.applicationContext, msg, Toast.LENGTH_LONG).show()
                getLocation.value = false
            }
        )
    }

    FindCityScreen(
        viewModel = viewModel,
        navigateToWeatherScreen = navigateToWeatherScreen,
        onGetLocationClick = {
            getLocation.value = true
        }
    )
}