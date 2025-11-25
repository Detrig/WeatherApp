package github.detrig.weatherapp.weather.presentation.models

import androidx.compose.ui.graphics.Color
import github.detrig.weatherapp.R
import github.detrig.weatherapp.weather.domain.models.AirQuality
import javax.inject.Inject

interface AirQualityUiMapper {
    fun map(domain: AirQuality): AirQualityUi

    class Base @Inject constructor() : AirQualityUiMapper {
        override fun map(domain: AirQuality): AirQualityUi {
            val pm25Level = MapAirParams.dangerLevelForPm25(domain.pm25)

            val title = when (pm25Level) {
                DangerLevel.GOOD -> R.string.good_air
                DangerLevel.MODERATE -> R.string.moderate_air
                DangerLevel.UNHEALTHY_SENSITIVE -> R.string.harmful_for_sensitive_people
                DangerLevel.UNHEALTHY -> R.string.harmful_for_everyone
                DangerLevel.VERY_UNHEALTHY -> R.string.very_harmful
                DangerLevel.HAZARDOUS -> R.string.dangerous
            }

            val subtitle = when (pm25Level) {
                DangerLevel.GOOD ->
                    "Можно спокойно гулять и заниматься спортом на улице"

                DangerLevel.MODERATE ->
                    "Небольшое загрязнение, у чувствительных людей возможен дискомфорт"

                DangerLevel.UNHEALTHY_SENSITIVE ->
                    "Людям с астмой и ХОБЛ стоит сократить время на улице"

                DangerLevel.UNHEALTHY ->
                    "Лучше избегать длительного пребывания на улице"

                DangerLevel.VERY_UNHEALTHY, DangerLevel.HAZARDOUS ->
                    "По возможности оставайтесь в помещении и закройте окна"
            }


            val overallColor = when (pm25Level) {
                DangerLevel.GOOD -> Color(0xFF4CAF50)       // зелёный
                DangerLevel.MODERATE -> Color(0xFFFFC107)   // жёлтый
                DangerLevel.UNHEALTHY_SENSITIVE -> Color(0xFFFF9800) // оранжевый
                DangerLevel.UNHEALTHY -> Color(0xFFF44336)  // красный
                DangerLevel.VERY_UNHEALTHY -> Color(0xFF9C27B0) // фиолетовый
                DangerLevel.HAZARDOUS -> Color(0xFF6A1B9A)  // тёмно-фиолетовый
            }


            return AirQualityUi(
                title = title,
                subtitle = subtitle,
                color = overallColor,
                pm25 = MapAirParams.mapPm25(domain.pm25),
                pm10 = MapAirParams.mapPm10(domain.pm10),
                no2 = MapAirParams.mapNo2(domain.no2),
                o3 = MapAirParams.mapO3(domain.o3),
                so2 = MapAirParams.mapSo2(domain.so2),
                co = MapAirParams.mapCo(domain.co),
            )
        }

    }


}

enum class DangerLevel {
    GOOD, MODERATE, UNHEALTHY_SENSITIVE, UNHEALTHY, VERY_UNHEALTHY, HAZARDOUS
}

object MapAirParams {

    fun mapPm25(value: Float): ParameterUi {
        val level = dangerLevelForPm25(value)
        return ParameterUi(
            name = "PM2.5",
            value = "${value.toInt()} µg/m³",
            dangerLevel = labelForLevel(level),
            icon = R.drawable.ic_pm25,
            color = colorForLevel(level)
        )
    }

    fun mapPm10(value: Float): ParameterUi {
        val level = dangerLevelForPm10(value)
        return ParameterUi(
            name = "PM10",
            value = "${value.toInt()} µg/m³",
            dangerLevel = labelForLevel(level),
            icon = R.drawable.ic_pm10,
            color = colorForLevel(level)
        )
    }

    fun mapNo2(value: Float): ParameterUi {
        val level = dangerLevelForNo2(value)
        return ParameterUi(
            name = "NO₂",
            value = "${value.toInt()} µg/m³",
            dangerLevel = labelForLevel(level),
            icon = R.drawable.ic_no2,
            color = colorForLevel(level)
        )
    }

    fun mapO3(value: Float): ParameterUi {
        val level = dangerLevelForO3(value)
        return ParameterUi(
            name = "O₃",
            value = "${value.toInt()} µg/m³",
            dangerLevel = labelForLevel(level),
            icon = R.drawable.ic_o3,
            color = colorForLevel(level)
        )
    }

    fun mapSo2(value: Float): ParameterUi {
        val level = dangerLevelForSo2(value)
        return ParameterUi(
            name = "SO₂",
            value = "${value.toInt()} µg/m³",
            dangerLevel = labelForLevel(level),
            icon = R.drawable.ic_so2,
            color = colorForLevel(level)
        )
    }

    fun mapCo(value: Float): ParameterUi {
        return ParameterUi(
            name = "CO",
            value = "${value.toInt()} µg/m³",
            dangerLevel = R.string.within_normal_street_values,
            icon = R.drawable.ic_co,
            color = Color.Gray
        )
    }

    fun dangerLevelForPm25(pm25: Float): DangerLevel =
        when {
            pm25 <= 10f -> DangerLevel.GOOD
            pm25 <= 25f -> DangerLevel.MODERATE
            pm25 <= 50f -> DangerLevel.UNHEALTHY_SENSITIVE
            pm25 <= 75f -> DangerLevel.UNHEALTHY
            pm25 <= 100f -> DangerLevel.VERY_UNHEALTHY
            else -> DangerLevel.HAZARDOUS
        }

    fun dangerLevelForPm10(pm10: Float): DangerLevel =
        when {
            pm10 <= 20f -> DangerLevel.GOOD
            pm10 <= 40f -> DangerLevel.MODERATE
            pm10 <= 60f -> DangerLevel.UNHEALTHY_SENSITIVE
            pm10 <= 100f -> DangerLevel.UNHEALTHY
            pm10 <= 150f -> DangerLevel.VERY_UNHEALTHY
            else -> DangerLevel.HAZARDOUS
        }

    fun dangerLevelForNo2(no2: Float): DangerLevel =
        when {
            no2 <= 40f -> DangerLevel.GOOD
            no2 <= 100f -> DangerLevel.UNHEALTHY_SENSITIVE
            no2 <= 200f -> DangerLevel.UNHEALTHY
            no2 <= 400f -> DangerLevel.VERY_UNHEALTHY
            else -> DangerLevel.HAZARDOUS
        }

    fun dangerLevelForO3(o3: Float): DangerLevel =
        when {
            o3 <= 50f -> DangerLevel.GOOD
            o3 <= 100f -> DangerLevel.MODERATE
            o3 <= 160f -> DangerLevel.UNHEALTHY_SENSITIVE
            o3 <= 240f -> DangerLevel.UNHEALTHY
            o3 <= 300f -> DangerLevel.VERY_UNHEALTHY
            else -> DangerLevel.HAZARDOUS
        }

    fun dangerLevelForSo2(so2: Float): DangerLevel =
        when {
            so2 <= 20f -> DangerLevel.GOOD
            so2 <= 80f -> DangerLevel.MODERATE
            so2 <= 250f -> DangerLevel.UNHEALTHY
            so2 <= 350f -> DangerLevel.VERY_UNHEALTHY
            else -> DangerLevel.HAZARDOUS
        }

    fun colorForLevel(level: DangerLevel): Color =
        when (level) {
            DangerLevel.GOOD -> Color(0xFF4CAF50)
            DangerLevel.MODERATE -> Color(0xFFFFC107)
            DangerLevel.UNHEALTHY_SENSITIVE -> Color(0xFFFF9800)
            DangerLevel.UNHEALTHY -> Color(0xFFF44336)
            DangerLevel.VERY_UNHEALTHY -> Color(0xFF9C27B0)
            DangerLevel.HAZARDOUS -> Color(0xFF6A1B9A)
        }

    fun labelForLevel(level: DangerLevel): Int =
        when (level) {
            DangerLevel.GOOD -> R.string.low_level
            DangerLevel.MODERATE -> R.string.moderate_level
            DangerLevel.UNHEALTHY_SENSITIVE -> R.string.increases_level
            DangerLevel.UNHEALTHY -> R.string.high_level
            DangerLevel.VERY_UNHEALTHY -> R.string.very_high_level
            DangerLevel.HAZARDOUS -> R.string.dangerous_level
        }
}