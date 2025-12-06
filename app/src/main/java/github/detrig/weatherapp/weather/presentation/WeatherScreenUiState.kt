package github.detrig.weatherapp.weather.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import github.detrig.weatherapp.R
import github.detrig.weatherapp.core.WeatherParamsParser
import github.detrig.weatherapp.custom_views.WeatherHorizontalGraph
import github.detrig.weatherapp.weather.presentation.models.WeatherInCityUi
import github.detrig.weatherapp.weather.presentation.ui.AirQualityUi
import github.detrig.weatherapp.weather.presentation.ui.ForecastUi
import github.detrig.weatherapp.weather.presentation.ui.LoadingUi
import github.detrig.weatherapp.weather.presentation.ui.WeatherUi
import java.io.Serializable

interface WeatherScreenUiState : Serializable {

    @Composable
    fun Show(onRetryClick: () -> Unit, onSettingClick: () -> Unit)

    data object Empty : WeatherScreenUiState {
        private fun readResolve(): Any = Empty

        @Composable
        override fun Show(onRetryClick: () -> Unit, onSettingClick: () -> Unit) = Unit
    }

    data class Base(private val weatherUi: WeatherInCityUi) : WeatherScreenUiState {

        @Composable
        override fun Show(onRetryClick: () -> Unit, onSettingClick: () -> Unit) {
            val backgroundModifier = WeatherParamsParser.backgroundForCondition(weatherUi.condition)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .then(backgroundModifier)
                    .verticalScroll(rememberScrollState())
            ) {
                WeatherUi(weatherUi, onSettingClick)
                Spacer(modifier = Modifier.height(16.dp))
                AirQualityUi(weatherUi.airQuality)
                Spacer(modifier = Modifier.height(16.dp))

                weatherUi.forecast.forEach {
                    WeatherHorizontalGraph(points = it.weatherForHour)
                    Spacer(modifier = Modifier.height(4.dp))
                }

                Spacer(modifier = Modifier.height(16.dp))
                ForecastUi(weatherUi.forecast, modifier = Modifier)
            }
        }
    }

    data object NoConnectionError : WeatherScreenUiState {
        private fun readResolve(): Any = NoConnectionError

        @Composable
        override fun Show(onRetryClick: () -> Unit, onSettingClick: () -> Unit) {
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

    data object Loading : WeatherScreenUiState {
        private fun readResolve(): Any = Loading

        @Composable
        override fun Show(onRetryClick: () -> Unit, onSettingClick: () -> Unit) {
            LoadingUi()
        }
    }

    data object GenericError : WeatherScreenUiState {
        private fun readResolve(): Any = GenericError

        @Composable
        override fun Show(onRetryClick: () -> Unit, onSettingClick: () -> Unit) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("genericErrorLayer"),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.sad),
                    modifier = Modifier.size(128.dp),
                    contentDescription = stringResource(R.string.unexpected_error)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = stringResource(R.string.unexpected_error))
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


