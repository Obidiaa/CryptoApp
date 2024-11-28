package com.obidia.cryptoapp.core.presentation.util

import java.text.NumberFormat
import java.util.Locale

data class DisplayableNumber(
    val value: Double,
    val formatted: String
)

fun Double.toDisplayableNumber(
    min: Int = 0,
    max: Int = 0
): DisplayableNumber {
    val format = NumberFormat.getNumberInstance(Locale.getDefault()).apply {
        minimumFractionDigits = min
        maximumFractionDigits = max
    }

    return DisplayableNumber(
        value = this,
        formatted = format.format(this)
    )
}
