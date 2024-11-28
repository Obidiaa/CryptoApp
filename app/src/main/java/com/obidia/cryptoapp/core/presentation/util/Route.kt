package com.obidia.cryptoapp.core.presentation.util

import kotlinx.serialization.Serializable

interface Route

@Serializable
data object CryptoListScreenRoute : Route

@Serializable
data class CryptoDetailScreenRoute(val id: String) : Route