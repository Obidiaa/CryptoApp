package com.obidia.cryptoapp.crypto.presentation.cryptodetail

import androidx.annotation.DrawableRes
import com.obidia.cryptoapp.core.presentation.util.DisplayableNumber
import com.obidia.cryptoapp.core.presentation.util.ErrorDataState
import com.obidia.cryptoapp.core.presentation.util.getDrawableIdForCrypto
import com.obidia.cryptoapp.core.presentation.util.toDisplayableNumber
import com.obidia.cryptoapp.crypto.domain.CryptoDetail

data class CryptoDetailState(
    val isCryptoDetailLoading: Boolean = false,
    val isCryptoHistoryLoading: Boolean = false,
    val cryptoDetailUi: CryptoDetailUi? = null,
    val listDataPoint: List<DataPoint> = emptyList(),
    val errorDataState: ErrorDataState? = null
)

data class CryptoDetailUi(
    val name: String,
    val symbol: String,
    val marketCap: DisplayableNumber,
    val price: DisplayableNumber,
    val changeLast24Hr: DisplayableNumber,
    @DrawableRes val iconRes: Int,
    val volume24h: DisplayableNumber,
    val supply: DisplayableNumber,
    val maxSupply: DisplayableNumber
)

fun CryptoDetail.toCryptoDetailUi(): CryptoDetailUi {
    return CryptoDetailUi(
        name = this.name,
        symbol = this.symbol,
        marketCap = this.marketCap.toDisplayableNumber(),
        price = this.price.toDisplayableNumber(),
        changeLast24Hr = absoluteChangeFormatted(this.changePercent24Hr, this.price),
        getDrawableIdForCrypto(this.symbol),
        this.volume24h.toDisplayableNumber(),
        this.supply.toDisplayableNumber(),
        this.maxSupply.toDisplayableNumber()
    )
}

fun absoluteChangeFormatted(change24Ht: Double, price: Double): DisplayableNumber {
    return (price * (change24Ht / 100)).toDisplayableNumber(4, 4)
}

data class DataPoint(
    val x: Float,
    val y: Float,
    val xLabel: String
)