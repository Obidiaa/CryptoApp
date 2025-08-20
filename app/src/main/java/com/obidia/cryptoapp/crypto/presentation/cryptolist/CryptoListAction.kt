package com.obidia.cryptoapp.crypto.presentation.cryptolist

sealed interface CryptoListAction {
    data object OnClickErrorBtn : CryptoListAction
    data object OnRefresh : CryptoListAction
}