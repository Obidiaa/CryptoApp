package com.obidia.cryptoapp.crypto.presentation.cryptodetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.obidia.cryptoapp.core.domain.util.onError
import com.obidia.cryptoapp.core.domain.util.onSuccess
import com.obidia.cryptoapp.crypto.domain.CryptoDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class CryptoDetailViewModel(
    private val dataSource: CryptoDataSource
) : ViewModel() {

    private val _state = MutableStateFlow(CryptoDetailState())
    val state get() = _state.asStateFlow()

    fun getDetailCoin(idCoin: String) {
        viewModelScope.launch {
            _state.update { it.copy(isCryptoDetailLoading = true) }

            dataSource.getCryptoDetail(idCoin).onSuccess { coinDetail ->
                _state.update {
                    it.copy(
                        isCryptoDetailLoading = false,
                        cryptoDetailUi = coinDetail.toCryptoDetailUi(),
                    )
                }
            }.onError {

            }
        }
    }

    fun action(event: CryptoDetailEvent) {
        when (event) {
            is CryptoDetailEvent.OnClickItem -> {
                getHistoryCoin(event.idCoin, event.interval)
            }
        }
    }

    fun getHistoryCoin(idCoin: String, interval: String) {
        _state.update { it.copy(isCryptoHistoryLoading = true) }

        viewModelScope.launch {
            dataSource
                .getHistory(
                    cryptoId = idCoin,
                    start = ZonedDateTime.now().minusDays(
                        when (interval) {
                            "1d" -> 1
                            "1w" -> 7
                            "1m" -> 30
                            "1y" -> 360
                            "5y" -> 1800
                            else -> 0
                        }
                    ),
                    end = ZonedDateTime.now(),
                    interval = interval
                )
                .onSuccess { history ->
                    val dataPoints = history
                        .sortedBy { it.dateTime }
                        .map {
                            DataPoint(
                                x = it.dateTime.hour.toFloat(),
                                y = it.priceUsd.toFloat(),
                                xLabel = DateTimeFormatter
                                    .ofPattern(
                                        when (interval) {
                                            "1d" -> "HH:mm"
                                            "1w" -> "E HH:mm"
                                            "1m" -> "d MMMM"
                                            "1y" -> "d MMMM yyyy"
                                            "5y" -> "d MMMM yyyy"
                                            else -> ""
                                        }
                                    )
                                    .format(it.dateTime)
                            )
                        }

                    _state.update {
                        it.copy(listDataPoint = dataPoints, isCryptoHistoryLoading = false)
                    }
                }
                .onError { error -> }
        }
    }
}