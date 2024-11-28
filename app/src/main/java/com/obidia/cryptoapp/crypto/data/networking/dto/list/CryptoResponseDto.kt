package com.obidia.cryptoapp.crypto.data.networking.dto.list

import com.obidia.cryptoapp.crypto.domain.Crypto
import kotlinx.serialization.Serializable

@Serializable
data class CryptoResponseDto(
    val data: List<CryptoDto>
)

@Serializable
data class CryptoDto(
    val id: String,
    val rank: Int,
    val name: String,
    val symbol: String,
    val marketCapUsd: Double,
    val priceUsd: Double,
    val changePercent24Hr: Double
)

fun CryptoDto.toCrypto(): Crypto = Crypto(
    id = id,
    rank = rank,
    name = name,
    symbol = symbol,
    marketCapUsd = marketCapUsd,
    priceUsd = priceUsd,
    changePercent24Hr = changePercent24Hr
)
