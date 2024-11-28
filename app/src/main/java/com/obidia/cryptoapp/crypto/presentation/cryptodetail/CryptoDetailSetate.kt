package com.obidia.cryptoapp.crypto.presentation.cryptodetail

import androidx.annotation.DrawableRes
import com.obidia.cryptoapp.core.presentation.util.getDrawableIdForCrypto
import com.obidia.cryptoapp.core.presentation.util.DisplayableNumber
import com.obidia.cryptoapp.core.presentation.util.toDisplayableNumber
import com.obidia.cryptoapp.crypto.domain.CryptoDetail

data class CryptoDetailState(
    val isCoinDetailLoading: Boolean = false,
    val isCoinHistoryLoading: Boolean = false,
    val coinDetailUi: CryptoDetailUi? = null,
    val listDataPoint: List<DataPoint> = emptyList()
)

data class CryptoDetailUi(
    val name: String,
    val symbol: String,
    val marketCap: DisplayableNumber,
    val price: DisplayableNumber,
    val changeLast24Hr: DisplayableNumber,
    @DrawableRes val iconRes: Int,
)

fun CryptoDetail.toCoinDetailUi(): CryptoDetailUi {
    return CryptoDetailUi(
        name = this.name,
        symbol = this.symbol,
        marketCap = this.marketCap.toDisplayableNumber(),
        price = this.price.toDisplayableNumber(),
        changeLast24Hr = absoluteChangeFormatted(this.changePercent24Hr, this.price),
        getDrawableIdForCrypto(this.symbol)
    )
}

fun absoluteChangeFormatted(change24Ht: Double, price: Double): DisplayableNumber {
    return (price * (change24Ht / 100)).toDisplayableNumber()
}

data class DataPoint(
    val x: Float,
    val y: Float,
    val xLabel: String
)