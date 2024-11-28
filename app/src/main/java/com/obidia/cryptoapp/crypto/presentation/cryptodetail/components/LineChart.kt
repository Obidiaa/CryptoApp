package com.obidia.cryptoapp.crypto.presentation.cryptodetail.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.obidia.cryptoapp.crypto.presentation.cryptodetail.DataPoint
import com.obidia.cryptoapp.crypto.presentation.cryptodetail.model.ChartStyle
import com.obidia.cryptoapp.crypto.presentation.cryptodetail.model.ValueLabel
import com.obidia.cryptoapp.ui.theme.CryptoAppTheme
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.abs
import kotlin.random.Random

@Composable
fun LineChart(
    dataPoints: List<DataPoint>,
    style: ChartStyle,
    visibleDataPointsIndices: IntRange,
    unit: String,
    modifier: Modifier = Modifier,
    selectedDataPoint: DataPoint? = null,
    onSelectedDataPoint: (DataPoint) -> Unit = {},
    showHelperLines: Boolean = true
) {
    val textStyle = LocalTextStyle.current.copy(
        fontSize = style.labelFontSize
    )

    val visibleDataPoints = remember(dataPoints, visibleDataPointsIndices) {
        dataPoints.slice(visibleDataPointsIndices)
    }

    val maxYValue = remember(visibleDataPoints) {
        visibleDataPoints.maxOfOrNull { it.y } ?: 0f
    }
    val minYValue = remember(visibleDataPoints) {
        visibleDataPoints.minOfOrNull { it.y } ?: 0f
    }

    val measurer = rememberTextMeasurer()

    val selectedDataPointIndex = remember(selectedDataPoint) {
        dataPoints.indexOf(selectedDataPoint)
    }
    var drawPoints by remember {
        mutableStateOf(listOf<DataPoint>())
    }

    val width = LocalConfiguration.current.screenWidthDp.dp - (style.horizontalPadding * 2)
    val density = LocalDensity.current
    val widthPx = with(density) {
        width.toPx()
    }
    val paddingHorizontal = with(density) {
        (style.horizontalPadding).toPx()
    }
    val range = (widthPx) / (visibleDataPoints.size - 1)

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(drawPoints) {
                detectHorizontalDragGestures { change, _ ->
                    val indexDataPoint = visibleDataPointsIndices.minByOrNull { dataPoint ->
                        abs(dataPoint * range + paddingHorizontal - change.position.x)
                    } ?: 0

                    onSelectedDataPoint(visibleDataPoints[indexDataPoint])
                }
            }
    ) {
        val verticalPaddingPx = style.verticalPadding.toPx()

        val xLabelTextLayoutResults = visibleDataPoints.map {
            measurer.measure(
                text = it.xLabel,
                style = textStyle.copy(textAlign = TextAlign.Center)
            )
        }
        val maxXLabelHeight = xLabelTextLayoutResults.maxOfOrNull { it.size.height } ?: 0
        val maxXLabelLineCount = xLabelTextLayoutResults.maxOfOrNull { it.lineCount } ?: 0
        val xLabelLineHeight = if (maxXLabelLineCount > 0) {
            maxXLabelHeight / maxXLabelLineCount
        } else 0

        val viewPortHeightPx = size.height -
                (maxXLabelHeight + 2 * verticalPaddingPx
                        + xLabelLineHeight)


        val viewPortTopY = verticalPaddingPx + xLabelLineHeight + 10f
        val viewPortBottomY = viewPortTopY + viewPortHeightPx

        xLabelTextLayoutResults.forEachIndexed { index, result ->
            if (selectedDataPointIndex == index) drawText(
                textLayoutResult = result,
                topLeft = Offset(
                    x = if (widthPx / 2 > (index * range + paddingHorizontal)) index * range + paddingHorizontal else index * range + paddingHorizontal - result.size.width,
                    y = viewPortBottomY
                ),
                color = style.selectedColor
            )

            if (showHelperLines && selectedDataPointIndex == index) {
                drawLine(
                    color = style.selectedColor,
                    start = Offset(
                        x = index * range + paddingHorizontal,
                        y = viewPortBottomY
                    ),
                    end = Offset(
                        x = index * range + paddingHorizontal,
                        y = viewPortTopY
                    ),
                    strokeWidth = style.helperLinesThicknessPx
                )
            }

            if (selectedDataPointIndex == index) {
                val valueLabel = ValueLabel(
                    value = visibleDataPoints[index].y,
                    unit = unit
                )
                val valueResult = measurer.measure(
                    text = valueLabel.formatted(),
                    style = textStyle.copy(
                        color = style.selectedColor
                    ),
                    maxLines = 1
                )
                drawText(
                    textLayoutResult = valueResult,
                    topLeft = Offset(
                        x = if (widthPx / 2 > (index * range + paddingHorizontal)) index * range + paddingHorizontal else index * range + paddingHorizontal - valueResult.size.width,
                        y = viewPortTopY - valueResult.size.height - 10f
                    )
                )
            }
        }

        drawPoints = visibleDataPointsIndices.map {
            val x = it * range + paddingHorizontal
            val ratio = (dataPoints[it].y - minYValue) / (maxYValue - minYValue)
            val y = viewPortBottomY - (ratio * viewPortHeightPx)
            DataPoint(
                x = x,
                y = y,
                xLabel = dataPoints[it].xLabel
            )
        }

        val conPoints1 = mutableListOf<DataPoint>()
        val conPoints2 = mutableListOf<DataPoint>()
        for (i in 1 until drawPoints.size) {
            val p0 = drawPoints[i - 1]
            val p1 = drawPoints[i]

            val x = (p1.x + p0.x) / 2f
            val y1 = p0.y
            val y2 = p1.y

            conPoints1.add(DataPoint(x, y1, ""))
            conPoints2.add(DataPoint(x, y2, ""))
        }

        val backgroundPath = Path().apply {
            if (drawPoints.isNotEmpty()) {
                moveTo(drawPoints.first().x, viewPortBottomY) // Mulai dari bawah sumbu X
                lineTo(drawPoints.first().x, drawPoints.first().y) // Ke titik pertama grafik

                for (i in 1 until drawPoints.size) {
                    lineTo(drawPoints[i].x, drawPoints[i].y)
                }

                lineTo(drawPoints.last().x, viewPortBottomY) // Tutup ke bawah sumbu X
                close()
            }
        }

        val linePath = Path().apply {
            if (drawPoints.isNotEmpty()) {
                moveTo(drawPoints.first().x, drawPoints.first().y)

                for (i in 1 until drawPoints.size) {
                    lineTo(drawPoints[i].x, drawPoints[i].y)
                }
            }
        }
        drawPath(
            path = backgroundPath,
            brush = Brush.verticalGradient(
                colors = listOf(
                    style.chartLineColor,
                    style.chartLineColorShadow
                ),
                startY = 0f,
                endY = viewPortBottomY
            ),
        )

        drawPath(
            path = linePath,
            color = style.chartLineColor,
            style = Stroke(
                width = style.axisLinesThicknessPx,
                cap = StrokeCap.Round
            )
        )

        drawPoints.forEachIndexed { index, point ->
            if (selectedDataPointIndex == index) {
                val circleOffset = Offset(
                    x = point.x,
                    y = point.y
                )

                drawCircle(
                    color = Color.White,
                    radius = 20f,
                    center = circleOffset
                )

                drawCircle(
                    color = style.selectedColor,
                    radius = 15f,
                    center = circleOffset
                )
            }
        }
    }
}

@Preview(device = Devices.NEXUS_5)
@Composable
private fun LineChartPreview() {
    CryptoAppTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            val cryptoHistoryRandomized = remember {
                (1..20).map {
                    DataPoint(
                        x = ZonedDateTime.now().plusHours(it.toLong()).hour.toFloat(),
                        y = (Random.nextFloat() * 1000.0).toFloat(),
                        xLabel = DateTimeFormatter
                            .ofPattern("ha M/d")
                            .format(ZonedDateTime.now().plusHours(it.toLong()))
                    )
                }
            }
            val style = ChartStyle(
                chartLineColor = MaterialTheme.colorScheme.primary,
                unselectedColor = Color(0xFF7C7C7C),
                selectedColor = MaterialTheme.colorScheme.primary,
                helperLinesThicknessPx = 5f,
                axisLinesThicknessPx = 5f,
                labelFontSize = 14.sp,
                verticalPadding = 8.dp,
                horizontalPadding = 16.dp,
                chartLineColorShadow = Color.White
            )
            LineChart(
                dataPoints = cryptoHistoryRandomized,
                style = style,
                visibleDataPointsIndices = cryptoHistoryRandomized.indices,
                unit = "$",
                modifier = Modifier
                    .width(700.dp)
                    .height(500.dp)
                    .background(Color.White),
                selectedDataPoint = cryptoHistoryRandomized[1]
            )
        }
    }
}