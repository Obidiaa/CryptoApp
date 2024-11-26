package com.obidia.cryptoapp.crypto.presentation.cryptodetail

sealed class CryptoDetailEvent {
    data class OnClickItem(val idCoin: String, val interval: String) : CryptoDetailEvent()
}