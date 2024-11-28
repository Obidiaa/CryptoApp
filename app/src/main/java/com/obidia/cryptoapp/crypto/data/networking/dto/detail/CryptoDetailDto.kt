package com.obidia.cryptoapp.crypto.data.networking.dto.detail

import com.obidia.cryptoapp.crypto.domain.CryptoDetail
import kotlinx.serialization.Serializable

@Serializable
data class CryptoDetailDto(
    val data: Data?,
    val timestamp: Long?
)

@Serializable
data class Data(
    val id: String?,
    val rank: Int?,
    val symbol: String?,
    val name: String?,
    val supply: String?,
    val maxSupply: String?,
    val marketCapUsd: Double?,
    val volumeUsd24Hr: String?,
    val priceUsd: Double?,
    val changePercent24Hr: Double?,
    val vwap24Hr: String?
)

fun CryptoDetailDto.toCoinDetail(): CryptoDetail = CryptoDetail(
    this.data?.name ?: "",
    this.data?.symbol ?: "",
    this.data?.marketCapUsd ?: 0.0,
    this.data?.priceUsd ?: 0.0,
    this.data?.changePercent24Hr ?: 0.0
)