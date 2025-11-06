package github.detrig.weatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import github.detrig.weatherapp.findcity.presentation.FindCityScreen
import github.detrig.weatherapp.findcity.presentation.FindCityViewModel
import github.detrig.weatherapp.ui.theme.WeatherAppTheme
import github.detrig.weatherapp.weather.presentation.WeatherScreen
import github.detrig.weatherapp.weather.presentation.WeatherViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeatherAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainContent(innerPadding)
                }
            }
        }
    }
}

@Composable
private fun MainContent(innerPadding: PaddingValues) {
    val navController: NavHostController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "findCityScreen",
        modifier = Modifier.padding(innerPadding)
    ) {
        composable("findCityScreen") {
            FindCityScreen(
                viewModel = hiltViewModel<FindCityViewModel>(),
                navigateToWeatherScreen = {
                    navController.navigate("weatherScreen")
                }
            )
        }

        composable("weatherScreen") {
            WeatherScreen(
                viewModel = hiltViewModel<WeatherViewModel>()
            )
        }
    }
}
