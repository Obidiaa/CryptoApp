package com.obidia.cryptoapp.crypto.domain

import kotlinx.serialization.Serializable

@Serializable
data class CryptoDetail(
    val name: String,
    val symbol: String,
    val marketCap: Double,
    val price: Double,
    val changePercent24Hr: Double,
    val volume24h: Double,
    val supply: Double,
    val maxSupply: Double
)
