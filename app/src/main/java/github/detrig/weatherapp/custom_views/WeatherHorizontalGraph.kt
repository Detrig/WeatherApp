package github.detrig.weatherapp.custom_views

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import github.detrig.weatherapp.weather.presentation.models.WeatherForHourUi

/**
size: Size — текущий размер области рисования в px.

center: Offset — центр области (Offset(size.width/2, size.height/2)).

density: Float и fontScale: Float — для пересчёта dp/sp.


 */

data class ChartConfig(
    val pointSpacing: Dp = 64.dp,          // расстояние между точками по X
    val chartHeight: Dp = 160.dp,
    val horizontalPadding: Dp = 16.dp,
    val lineColor: Color = Color.LightGray,
    val pointColor: Color = Color.Gray,
    val showBaseline: Boolean = true
)

@Composable
fun WeatherHorizontalGraph(
    points: List<WeatherForHourUi>,
    modifier: Modifier = Modifier,
    config: ChartConfig = ChartConfig()
) {
    val scrollState = rememberScrollState()
    val textMeasurer = rememberTextMeasurer()

    val pointCount = points.size
    //Normalize
    val allValues = points.map { it.tempValue }
    val maxValue = allValues.max()
    val minValue = allValues.min()
    val valueRange = (maxValue - minValue).takeIf { it != 0f } ?: 1f

    // Рассчитываем ширину Canvas: паддинги + расстояние между точками
    val canvasWidth = if (pointCount <= 1) {
        config.horizontalPadding * 2
    } else {
        config.horizontalPadding * 2 + config.pointSpacing * (pointCount - 1)
    }

    // 1) График (Canvas + горизонтальный скролл)
    Box(
        modifier = modifier
            .height(config.chartHeight)
            .horizontalScroll(scrollState)
    ) {
        Canvas(
            modifier = Modifier
                .height(config.chartHeight)
                .padding(vertical = 8.dp, horizontal = 8.dp)
                .width(canvasWidth)
        ) {
            val paddingStart = config.horizontalPadding.toPx()
            val paddingEnd = config.horizontalPadding.toPx()
            val paddingTop = 16.dp.toPx()
            val paddingBottom = 24.dp.toPx()

            val contentHeight = size.height - paddingTop - paddingBottom
            val pointSpacingPx = config.pointSpacing.toPx()


            var currentX = 0f + paddingStart

            val offsetPointsMap = LinkedHashMap<Offset, WeatherForHourUi>()
            points.forEach {
                val normalized = (it.tempValue - minValue) / valueRange
                val x = currentX
                val y = paddingTop + (1f - normalized) * contentHeight
                offsetPointsMap.put(Offset(x, y), it)
                currentX += pointSpacingPx
            }


            //LINES
            val offsets = offsetPointsMap.keys.toList()
            if (offsets.size >= 2) {
                val path = buildSmoothPath(offsets, smoothness = 1f)

                drawPath(
                    path = path,
                    color = config.lineColor,
                    style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
                )
            }


            //КРУЖОЧКИ
            val outerRadius = 6.dp.toPx()
            val labelPadding = 6.dp.toPx() // отступ между текстом и кружком

            offsetPointsMap.forEach {

                // 1) измеряем текст
                val textTempLayoutResult = textMeasurer.measure(
                    text = AnnotatedString(it.value.tempText),
                    style = TextStyle(
                        fontSize = 10.sp,
                        color = Color.Black
                    )
                )

                // 2) считаем позицию текста: по X центрируем над кружком,
                // по Y — над кружком с небольшим отступом
                val textTempX = it.key.x - textTempLayoutResult.size.width / 2f
                val textTempY =
                    it.key.y - outerRadius - labelPadding - textTempLayoutResult.size.height

                drawText(
                    textLayoutResult = textTempLayoutResult,
                    topLeft = Offset(textTempX, textTempY)
                )
                drawCircle(
                    color = config.pointColor,
                    radius = outerRadius,
                    center = it.key
                )


                val textTimeLayoutResult = textMeasurer.measure(
                    text = AnnotatedString(it.value.time),
                    style = TextStyle(
                        fontSize = 10.sp,
                        color = Color.Gray
                    )
                )
                val textTimeX = it.key.x - textTimeLayoutResult.size.width / 2f
                val textTimeY = size.minDimension - 32f
                drawText(
                    textLayoutResult = textTimeLayoutResult,
                    topLeft = Offset(textTimeX, textTimeY)
                )
            }
        }

    }
}


@Preview(showBackground = true)
@Composable
fun PreviewSome() {
    WeatherHorizontalGraph(
        listOf(
            WeatherForHourUi(
                time = "22:00",
                tempValue = 0f,
                tempText = "0*",
                windSpeed = "5f",
                chanceOfRain = "",
                chanceOfSnow = "",
                cloud = 0f
            ),
            WeatherForHourUi(
                time = "23:00",
                tempValue = 2f,
                tempText = "2*",
                windSpeed = "5f",
                chanceOfRain = "",
                chanceOfSnow = "",
                cloud = 0f
            ),
            WeatherForHourUi(
                time = "24:00",
                tempValue = 2f,
                tempText = "2*",
                windSpeed = "5f",
                chanceOfRain = "",
                chanceOfSnow = "",
                cloud = 0f
            )
        )
    )
}

//кубические Bezie (сплайн через все точки)
private fun buildSmoothPath(
    points: List<Offset>,
    smoothness: Float = 1f // 0..1, чем больше — тем сильнее сглаживание
): Path {
    val path = Path()
    if (points.isEmpty()) return path
    if (points.size == 1) {
        path.moveTo(points[0].x, points[0].y)
        return path
    }

    path.moveTo(points[0].x, points[0].y)

    for (i in 0 until points.size - 1) {
        val p0 = if (i - 1 >= 0) points[i - 1] else points[i]
        val p1 = points[i]
        val p2 = points[i + 1]
        val p3 = if (i + 2 < points.size) points[i + 2] else points[i + 1]

        // Catmull-Rom → cubic Bezier
        val cp1 = Offset(
            x = p1.x + (p2.x - p0.x) / 6f * smoothness,
            y = p1.y + (p2.y - p0.y) / 6f * smoothness
        )
        val cp2 = Offset(
            x = p2.x - (p3.x - p1.x) / 6f * smoothness,
            y = p2.y - (p3.y - p1.y) / 6f * smoothness
        )

        path.cubicTo(
            cp1.x, cp1.y,
            cp2.x, cp2.y,
            p2.x, p2.y
        )
    }

    return path
}