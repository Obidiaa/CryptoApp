package com.obidia.cryptoapp.crypto.presentation.cryptodetail.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit

data class ChartStyle(
    val chartLineColor: Color,
    val unselectedColor: Color,
    val selectedColor: Color,
    val helperLinesThicknessPx: Float,
    val axisLinesThicknessPx: Float,
    val labelFontSize: TextUnit,
    val verticalPadding: Dp,
    val horizontalPadding: Dp,
    val chartLineColorShadow: Color,
)

data class ValueLabel(
    val value: Float,
    val unit: String
) {
    fun formatted(): String {
        return "$value $unit"
    }
}