package com.obidia.cryptoapp.crypto.data.networking.dto.detail

import com.obidia.cryptoapp.crypto.domain.CryptoPrice
import kotlinx.serialization.Serializable
import java.time.Instant
import java.time.ZoneId

@Serializable
data class CoinHistoryDto(
    val data: List<CoinPriceDto>
)

@Serializable
data class CoinPriceDto(
    val priceUsd: Double,
    val time: Long
)

fun CoinPriceDto.toCoinPrice(): CryptoPrice {
    return CryptoPrice(
        priceUsd = priceUsd,
        dateTime = Instant.ofEpochMilli(time).atZone(ZoneId.systemDefault())
    )
}
