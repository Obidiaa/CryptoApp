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
    val supply: Double?,
    val maxSupply: Double?,
    val marketCapUsd: Double?,
    val volumeUsd24Hr: Double?,
    val priceUsd: Double?,
    val changePercent24Hr: Double?,
    val vwap24Hr: String?
)

fun CryptoDetailDto.toCryptoDetail(): CryptoDetail = CryptoDetail(
    this.data?.name ?: "",
    this.data?.symbol ?: "",
    this.data?.marketCapUsd ?: 0.0,
    this.data?.priceUsd ?: 0.0,
    this.data?.changePercent24Hr ?: 0.0,
    volume24h = this.data?.volumeUsd24Hr ?: 0.0,
    supply = this.data?.supply ?: 0.0,
    maxSupply = this.data?.maxSupply ?: 0.0
)