package com.obidia.cryptoapp.crypto.presentation.cryptolist

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Immutable
import com.obidia.crypto.core.presentation.util.getDrawableIdForCoin
import com.obidia.cryptoapp.core.presentation.util.DisplayableNumber
import com.obidia.cryptoapp.core.presentation.util.toDisplayableNumber
import com.obidia.cryptoapp.crypto.domain.Crypto

@Immutable
data class CryptoListState(
    val isLoading: Boolean = true,
    val coins: List<CryptoItemUi> = emptyList(),
    val selectedCoin: CryptoItemUi? = null
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
    iconRes = getDrawableIdForCoin(symbol)
)
