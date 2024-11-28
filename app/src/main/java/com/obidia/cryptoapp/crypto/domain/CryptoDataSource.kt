package com.obidia.cryptoapp.crypto.domain

import com.obidia.cryptoapp.core.domain.util.NetworkError
import com.obidia.cryptoapp.core.domain.util.Result
import kotlinx.coroutines.flow.Flow
import java.time.ZonedDateTime

interface CryptoDataSource {
    suspend fun getCryptoList(): Result<Flow<List<Crypto>>, NetworkError>
    suspend fun getHistory(
        coinId: String,
        start: ZonedDateTime,
        end: ZonedDateTime,
        interval: String
    ): Result<List<CryptoPrice>, NetworkError>

    suspend fun getCryptoDetail(coinId: String): Result<CryptoDetail, NetworkError>
}