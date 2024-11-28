package com.obidia.cryptoapp.crypto.presentation.cryptolist

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Immutable
import com.obidia.cryptoapp.core.presentation.util.getDrawableIdForCrypto
import com.obidia.cryptoapp.core.presentation.util.DisplayableNumber
import com.obidia.cryptoapp.core.presentation.util.toDisplayableNumber
import com.obidia.cryptoapp.crypto.domain.Crypto

@Immutable
data class CryptoListState(
    val isLoading: Boolean = true,
    val cryptoList: List<CryptoItemUi> = emptyList(),
    val selectedCrypto: CryptoItemUi? = null
)

data class CryptoItemUi(
    val id: String,
    val rank: Int,
    val name: String,
    val symbol: String,
    val marketCapUsd: DisplayableNumber,
    val priceUsd: DisplayableNumber,
    val changePercent24Hr: DisplayableNumber,
    @DrawableRes val iconRes: Int
)

fun Crypto.toCryptoItemUi(): CryptoItemUi = CryptoItemUi(
    id = id,
    rank = rank,
    name = name,
    symbol = symbol,
    marketCapUsd = marketCapUsd.toDisplayableNumber(),
    priceUsd = priceUsd.toDisplayableNumber(),
    changePercent24Hr = changePercent24Hr.toDisplayableNumber(),
    iconRes = getDrawableIdForCrypto(symbol)
)
