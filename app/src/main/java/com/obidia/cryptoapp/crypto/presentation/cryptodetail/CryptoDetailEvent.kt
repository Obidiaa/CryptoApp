package com.obidia.cryptoapp.crypto.presentation.cryptodetail

sealed interface CryptoDetailEvent {
    data class OnClickItem(val interval: String) : CryptoDetailEvent
    data object OnClickErrorBtn : CryptoDetailEvent
}