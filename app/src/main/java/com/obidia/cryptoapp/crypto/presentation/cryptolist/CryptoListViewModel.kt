package com.obidia.cryptoapp.crypto.presentation.cryptolist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.obidia.cryptoapp.core.domain.util.onError
import com.obidia.cryptoapp.core.domain.util.onSuccess
import com.obidia.cryptoapp.crypto.domain.CryptoDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CryptoListViewModel(
    private val dataSource: CryptoDataSource
) : ViewModel() {

    private val _state = MutableStateFlow(CryptoListState())
    val state = _state.onStart {
        loadCoins()
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        CryptoListState()
    )

    private fun loadCoins() = viewModelScope.launch {
        dataSource.getCoins().onSuccess { state ->
            state.onEach { listCrypto ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        coins = listCrypto.map { crypto -> crypto.toCryptoItemUi() }
                    )
                }
            }.onStart {
                _state.update { it.copy(isLoading = true) }
            }.collect()
        }.onError { error ->
            _state.update {
                it.copy(isLoading = false)
            }
        }
    }

    fun action(event: CoinListAction) {
        when (event) {
            is CoinListAction.OnCoinClick -> {}
            CoinListAction.OnRefresh -> {}
        }
    }
}