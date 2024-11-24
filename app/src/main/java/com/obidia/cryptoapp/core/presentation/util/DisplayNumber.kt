package com.obidia.cryptoapp.core.presentation.util

import java.text.NumberFormat
import java.util.Locale

data class DisplayableNumber(
    val value: Double,
    val formatted: String
)

fun Double.toDisplayableNumber(): DisplayableNumber {
    val format = NumberFormat.getNumberInstance(Locale.getDefault()).apply {
        minimumFractionDigits = 2
        maximumFractionDigits = 2
    }

    return DisplayableNumber(
        value = this,
        formatted = format.format(this)
    )
}
