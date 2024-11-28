package com.obidia.cryptoapp.crypto.data.networking.dto.detail

import com.obidia.cryptoapp.crypto.domain.CryptoPrice
import kotlinx.serialization.Serializable
import java.time.Instant
import java.time.ZoneId

@Serializable
data class CryptoHistoryDto(
    val data: List<CryptoPriceDto>
)

@Serializable
data class CryptoPriceDto(
    val priceUsd: Double,
    val time: Long
)

fun CryptoPriceDto.toCryptoPrice(): CryptoPrice {
    return CryptoPrice(
        priceUsd = priceUsd,
        dateTime = Instant.ofEpochMilli(time).atZone(ZoneId.systemDefault())
    )
}
