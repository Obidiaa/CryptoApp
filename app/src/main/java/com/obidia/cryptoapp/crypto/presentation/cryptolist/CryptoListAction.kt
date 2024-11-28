package com.obidia.cryptoapp.crypto.presentation.cryptolist

sealed interface CryptoListAction {
    data class OnCryptoClick(val data: CryptoItemUi) : CryptoListAction
    data object OnRefresh : CryptoListAction
}