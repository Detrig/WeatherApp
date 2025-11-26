package github.detrig.weatherapp.weather.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Text
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import github.detrig.weatherapp.R
import github.detrig.weatherapp.weather.presentation.models.AirQualityUiModel
import github.detrig.weatherapp.weather.presentation.models.ParameterUi

@Composable
fun AirQualityUi(airQualityUi: AirQualityUiModel) {
    val parameters = listOf(
        airQualityUi.pm25,
        airQualityUi.pm10,
        airQualityUi.no2,
        airQualityUi.o3,
        airQualityUi.so2,
        airQualityUi.co
    )
    val title = stringResource(airQualityUi.title)

    Column(modifier = Modifier.fillMaxWidth()) {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp, start = 8.dp, end = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(parameters) { item ->
                AirParameter(item)
            }
        }

        Text(
            text = title,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontSize = 12.sp
        )

        Text(
            text = airQualityUi.subtitle,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            style = TextStyle(lineHeight = 14.sp),
            textAlign = TextAlign.Start,
            fontSize = 11.sp
        )
    }
}

@Composable
fun AirParameter(airQualityParameter: ParameterUi) {
    Column(
        modifier = Modifier
            .background(airQualityParameter.color, RoundedCornerShape(4.dp))
            .padding(4.dp)
    ) {
        Text(
            text = airQualityParameter.name,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(Modifier.height(4.dp))
        Image(
            painter = painterResource(airQualityParameter.icon),
            contentDescription = airQualityParameter.name,
            modifier = Modifier
                .size(24.dp)
                .align(Alignment.CenterHorizontally),
            contentScale = ContentScale.Crop
        )
        Spacer(Modifier.height(4.dp))
        Column(
            Modifier
                .padding(2.dp)
        ) {
            Text(text = airQualityParameter.value, fontSize = 12.sp)
        }
    }

}

@Preview(showBackground = true)
@Composable
fun PreviewAirQualityUi() {
    AirQualityUi(
        AirQualityUiModel(
            title = R.string.harmful_for_sensitive_people,
            subtitle = "Людям с астмой лучше сократитsь время на улице",
            color = Color(0xFFFF9800),
            pm25 = ParameterUi(
                name = "PM2.5",
                value = "48 µg/m³",
                dangerLevel = R.string.high_level,
                icon = R.drawable.ic_pm25,
                color = Color(0xFFFF9800)
            ),
            pm10 = ParameterUi(
                name = "PM10",
                value = "59 µg/m³",
                dangerLevel = R.string.moderate_air,
                icon = R.drawable.ic_pm10,
                color = Color(0xFFFFC107)
            ),
            no2 = ParameterUi(
                name = "NO₂",
                value = "73 µg/m³",
                dangerLevel = R.string.moderate_air,
                icon = R.drawable.ic_no2,
                color = Color(0xFFFFC107)
            ),
            o3 = ParameterUi(
                name = "O₃",
                value = "4 µg/m³",
                dangerLevel = R.string.good_air,
                icon = R.drawable.ic_o3,
                color = Color(0xFF4CAF50)
            ),
            so2 = ParameterUi(
                name = "SO₂",
                value = "47 µg/m³",
                dangerLevel = R.string.moderate_air,
                icon = R.drawable.ic_so2,
                color = Color(0xFFFFC107)
            ),
            co = ParameterUi(
                name = "CO",
                value = "562 µg/m³",
                dangerLevel = R.string.good_air,
                icon = R.drawable.ic_co,
                color = Color(0xFF4CAF50)
            )
        )
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewAirParameter() {
    AirParameter(
        ParameterUi(
            name = "NO₂",
            value = "73 µg/m³",
            dangerLevel = R.string.moderate_air,
            icon = R.drawable.ic_no2,
            color = Color(0xFFFFC107)
        )
    )
}