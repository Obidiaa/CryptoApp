package com.obidia.cryptoapp.crypto.presentation.cryptolist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.obidia.cryptoapp.core.domain.util.onError
import com.obidia.cryptoapp.core.domain.util.onLoading
import com.obidia.cryptoapp.core.domain.util.onSuccess
import com.obidia.cryptoapp.core.presentation.util.toErrorDataState
import com.obidia.cryptoapp.crypto.domain.CryptoDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingCommand
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.dropWhile
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CryptoListViewModel(
    private val dataSource: CryptoDataSource
) : ViewModel() {

    private val _state = MutableStateFlow(CryptoListState())
    val state: RestartableStateFlow<CryptoListState> = _state.onStart {
        loadCoins()
    }.restartableStateIn(
        viewModelScope,
        initialValue = CryptoListState()
    )

    private fun loadCoins() {
        viewModelScope.launch {
            dataSource.getCryptoList().collect { state ->
                state.onSuccess { data ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            cryptoList = data.map { crypto -> crypto.toCryptoItemUi() },
                            errorState = null
                        )
                    }
                }.onError { error ->
                    _state.update {
                        it.copy(isLoading = false, errorState = error.toErrorDataState())
                    }
                }.onLoading {
                    _state.update { it.copy(isLoading = true) }
                }
            }
        }
    }

    fun action(event: CryptoListAction) {
        when (event) {
            CryptoListAction.OnClickErrorBtn, CryptoListAction.OnRefresh -> {
                _state.update {
                    it.copy(errorState = null, isLoading = true)
                }
                state.restart()
            }
        }
    }
}

interface SharingRestartable : SharingStarted {
    fun restart()
}

// impementation of the sharing restartable
private data class SharingRestartableImpl(
    private val sharingStarted: OnetimeWhileSubscribed,
) : SharingRestartable {

    private val restartFlow = MutableSharedFlow<SharingCommand>(extraBufferCapacity = 2)

    // combine the commands from the restartFlow and the subscriptionCount
    override fun command(subscriptionCount: StateFlow<Int>): Flow<SharingCommand> {
        return merge(restartFlow, sharingStarted.command(subscriptionCount))
    }

    // stop and reset the replay cache and restart
    override fun restart() {
        restartFlow.tryEmit(SharingCommand.STOP_AND_RESET_REPLAY_CACHE)
        sharingStarted.onRefresh()
        restartFlow.tryEmit(SharingCommand.START)
    }
}

interface RestartableStateFlow<out T> : StateFlow<T> {
    fun restart()
}

// create a hot flow, which is restartable by manually from a cold flow
fun <T> Flow<T>.restartableStateIn(
    scope: CoroutineScope,
    initialValue: T
): RestartableStateFlow<T> {
    val sharingRestartable = SharingRestartableImpl(
        OnetimeWhileSubscribed(5000)
    )

    val stateFlow = stateIn(scope, sharingRestartable, initialValue)
    return object : RestartableStateFlow<T>, StateFlow<T> by stateFlow {
        override fun restart() = sharingRestartable.restart()
    }
}

class OnetimeWhileSubscribed(
    private val stopTimeout: Long,
    private val replayExpiration: Long = Long.MAX_VALUE,
) : SharingStarted {
    private val hasCollected: MutableStateFlow<Boolean> = MutableStateFlow(false)

    init {
        require(stopTimeout >= 0) { "stopTimeout($stopTimeout ms) cannot be negative" }
        require(replayExpiration >= 0) { "replayExpiration($replayExpiration ms) cannot be negative" }
    }

    fun onRefresh() {
        hasCollected.value = false
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun command(subscriptionCount: StateFlow<Int>): Flow<SharingCommand> =
        combine(hasCollected, subscriptionCount) { collected, counts ->
            collected to counts
        }.transformLatest { pair ->
                val (collected, count) = pair
                if (count > 0 && !collected) {
                    emit(SharingCommand.START)
                    hasCollected.value = true
                } else {
                    delay(stopTimeout)
                    if (replayExpiration > 0) {
                        emit(SharingCommand.STOP)
                        delay(replayExpiration)
                    }
                }
            }
            .dropWhile {
                it != SharingCommand.START
            } // don't emit any STOP/RESET_BUFFER to start with, only START
            .distinctUntilChanged() // just in case somebody forgets it, don't leak our multiple sending of START
}