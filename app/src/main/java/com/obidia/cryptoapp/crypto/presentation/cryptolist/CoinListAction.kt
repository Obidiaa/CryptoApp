package com.obidia.cryptoapp.crypto.presentation.cryptolist

sealed interface CoinListAction {
    data class OnCoinClick(val data: CryptoItemUi) : CoinListAction
    data object OnRefresh : CoinListAction
}