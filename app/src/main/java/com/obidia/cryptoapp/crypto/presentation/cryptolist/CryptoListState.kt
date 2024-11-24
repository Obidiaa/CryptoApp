package com.obidia.cryptoapp.crypto.presentation.cryptolist

import androidx.annotation.DrawableRes
import com.obidia.cryptoapp.core.presentation.util.DisplayableNumber

data class CryptoListState(
    val isLoading: Boolean = false,
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
