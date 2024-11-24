package com.obidia.cryptoapp.crypto.domain

import java.time.ZonedDateTime

data class CryptoPrice(
    val priceUsd: Double,
    val dateTime: ZonedDateTime
)
