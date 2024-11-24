package com.obidia.cryptoapp.ui.theme

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.obidia.cryptoapp.R

val RobotoMono = FontFamily(
    Font(
        resId = R.font.roboto_mono_reguler,
        weight = FontWeight.Normal
    ),
    Font(
        resId = R.font.roboto_mono_italic,
        weight = FontWeight.Normal,
        style = FontStyle.Italic
    ),
    Font(
        resId = R.font.roboto_mono_bold,
        weight = FontWeight.Bold
    ),
    Font(
        resId = R.font.roboto_mono_bold_italic,
        weight = FontWeight.Bold,
        style = FontStyle.Italic
    ),
)