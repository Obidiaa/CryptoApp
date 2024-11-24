package com.obidia.cryptoapp.crypto.domain

import com.obidia.cryptoapp.core.domain.util.NetworkError
import com.obidia.cryptoapp.core.domain.util.Result
import java.time.ZonedDateTime

interface CryptoDataSource {
    suspend fun getCoins(): Result<List<Crypto>, NetworkError>
    suspend fun getHistory(
        coinId: String,
        start: ZonedDateTime,
        end: ZonedDateTime,
        interval: String
    ): Result<List<CryptoPrice>, NetworkError>

    suspend fun getCoinDetail(coinId: String): Result<CryptoDetail, NetworkError>
}