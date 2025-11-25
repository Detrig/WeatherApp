package github.detrig.weatherapp.weather.presentation

import github.detrig.weatherapp.weather.domain.WeatherInCity
import github.detrig.weatherapp.weather.domain.WeatherResult
import javax.inject.Inject

class WeatherUiMapper @Inject constructor(): WeatherResult.Mapper<WeatherScreenUi> {

    override fun mapWeatherInCity(weatherInCity: WeatherInCity): WeatherScreenUi {
        return WeatherScreenUi.Base(weatherInCity)
    }

    override fun mapEmpty(): WeatherScreenUi {
        return WeatherScreenUi.Empty
    }

    override fun mapNoInternetError(): WeatherScreenUi {
        return WeatherScreenUi.NoConnectionError
    }

    override fun mapGenericError(): WeatherScreenUi {
        return WeatherScreenUi.GenericError
    }

    override fun mapLoading(): WeatherScreenUi {
        return WeatherScreenUi.Loading
    }

}