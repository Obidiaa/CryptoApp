package com.obidia.cryptoapp.crypto.presentation.cryptodetail

sealed class CryptoDetailEvent {
    data class OnClickItem(val interval: String) : CryptoDetailEvent()
}