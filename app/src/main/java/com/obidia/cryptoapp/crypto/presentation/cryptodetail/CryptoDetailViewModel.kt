package com.obidia.cryptoapp.crypto.presentation.cryptodetail

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.obidia.cryptoapp.core.domain.util.onError
import com.obidia.cryptoapp.core.domain.util.onSuccess
import com.obidia.cryptoapp.core.presentation.util.CryptoDetailScreenRoute
import com.obidia.cryptoapp.core.presentation.util.toErrorDataState
import com.obidia.cryptoapp.crypto.domain.CryptoDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class CryptoDetailViewModel(
    private val dataSource: CryptoDataSource,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val cryptoId = savedStateHandle.toRoute<CryptoDetailScreenRoute>().id
    private val _state = MutableStateFlow(CryptoDetailState())
    val state = _state.onStart {
        getDetailCoin()
        getHistoryCoin(cryptoId, "1d")
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        CryptoDetailState()
    )

    private fun getDetailCoin() = viewModelScope.launch {
        _state.update { it.copy(isCryptoDetailLoading = true) }

        dataSource.getCryptoDetail(cryptoId).onSuccess { coinDetail ->
            _state.update {
                it.copy(
                    isCryptoDetailLoading = false,
                    cryptoDetailUi = coinDetail.toCryptoDetailUi(),
                )
            }
        }.onError { error ->
            Log.d("kesini", error.toErrorDataState().toString())
            _state.update {
                it.copy(
                    errorDataState = error.toErrorDataState(),
                    isCryptoDetailLoading = false,
                )
            }
        }
    }

    fun action(event: CryptoDetailEvent) {
        when (event) {
            is CryptoDetailEvent.OnClickItem -> {
                getHistoryCoin(cryptoId, event.interval)
            }
        }
    }

    private fun getHistoryCoin(idCoin: String, interval: String) {
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