package com.obidia.cryptoapp.crypto.presentation.cryptolist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.obidia.cryptoapp.core.domain.util.onError
import com.obidia.cryptoapp.core.domain.util.onSuccess
import com.obidia.cryptoapp.core.presentation.util.toErrorDataState
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
        dataSource.getCryptoList().onSuccess { state ->
            state.onEach { listCrypto ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        cryptoList = listCrypto.map { crypto -> crypto.toCryptoItemUi() },
                        errorState = null
                    )
                }
            }.onStart {
                _state.update { it.copy(isLoading = true, errorState = null) }
            }.collect()
        }.onError { error ->
            _state.update {
                it.copy(isLoading = false, errorState = error.toErrorDataState())
            }
        }
    }

    fun action(event: CryptoListAction) {
        when (event) {
            CryptoListAction.OnClickErrorBtn -> {
                _state.update {
                    it.copy(errorState = null, isLoading = true)
                }
                loadCoins()
            }
        }
    }
}