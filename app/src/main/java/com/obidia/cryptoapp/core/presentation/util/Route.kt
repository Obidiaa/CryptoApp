package com.obidia.cryptoapp.core.presentation.util

import kotlinx.serialization.Serializable

interface Route

@Serializable
data object CoinListScreenRoute : Route

@Serializable
data class CoinDetailScreenRoute(val id: String) : Route