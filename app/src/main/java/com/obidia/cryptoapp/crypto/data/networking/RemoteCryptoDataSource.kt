package com.obidia.cryptoapp.crypto.data.networking

import com.obidia.cryptoapp.core.data.networking.constructUrl
import com.obidia.cryptoapp.core.data.networking.safeCall
import com.obidia.cryptoapp.core.domain.util.NetworkError
import com.obidia.cryptoapp.core.domain.util.Result
import com.obidia.cryptoapp.core.domain.util.map
import com.obidia.cryptoapp.crypto.data.networking.dto.detail.CryptoDetailDto
import com.obidia.cryptoapp.crypto.data.networking.dto.detail.CoinHistoryDto
import com.obidia.cryptoapp.crypto.data.networking.dto.detail.toCoinDetail
import com.obidia.cryptoapp.crypto.data.networking.dto.detail.toCoinPrice
import com.obidia.cryptoapp.crypto.data.networking.dto.list.CryptoResponseDto
import com.obidia.cryptoapp.crypto.data.networking.dto.list.toCoin
import com.obidia.cryptoapp.crypto.domain.CryptoDataSource
import com.obidia.cryptoapp.crypto.domain.CryptoDetail
import com.obidia.cryptoapp.crypto.domain.CryptoPrice
import com.obidia.cryptoapp.crypto.domain.Crypto
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.ZoneId
import java.time.ZonedDateTime

class RemoteCryptoDataSource(private val httpClient: HttpClient) : CryptoDataSource {
    override suspend fun getCoins(): Result<Flow<List<Crypto>>, NetworkError> {
        return safeCall<CryptoResponseDto> {
            httpClient.get(urlString = constructUrl("/assets"))
        }.map { response ->
            flow { emit(response.data.map { it.toCoin() }) }
        }
    }

    override suspend fun getCoinDetail(coinId: String): Result<CryptoDetail, NetworkError> {
        return safeCall<CryptoDetailDto> {
            httpClient.get(constructUrl("/assets/$coinId"))
        }.map { response ->
            response.toCoinDetail()
        }
    }

    override suspend fun getHistory(
        coinId: String,
        start: ZonedDateTime,
        end: ZonedDateTime,
        interval: String
    ): Result<List<CryptoPrice>, NetworkError> {
        val intervalCoin = when (interval) {
            "1d" -> "h1"
            "1w" -> "h1"
            "1m" -> "d1"
            "1y" -> "d1"
            "5y" -> "d1"
            else -> ""
        }
        val startMillis = start
            .withZoneSameInstant(ZoneId.of("UTC"))
            .toInstant()
            .toEpochMilli()

        val endMillis = end
            .withZoneSameInstant(ZoneId.of("UTC"))
            .toInstant()
            .toEpochMilli()

        return safeCall<CoinHistoryDto> {
            httpClient.get(
                urlString = constructUrl("/assets/$coinId/history")
            ) {
                parameter("interval", intervalCoin)
                parameter("start", startMillis)
                parameter("end", endMillis)
            }
        }.map { response ->
            response.data.map { it.toCoinPrice() }
        }
    }
}