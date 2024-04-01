package com.baris.fakestore.core

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update

/**
 * Created on 26.02.2024.
 * @author saycicek
 */
abstract class BaseViewModel<State : IUiState, Event : IUiEvent> : ViewModel() {

    private val _state by lazy {
        MutableStateFlow(initialState())
    }
    val state = _state.asStateFlow()

    private val _event = Channel<Event>()
    val event = _event.receiveAsFlow()

    abstract fun initialState(): State

    protected fun updateUiState(uiState: State) {
        _state.update { uiState }
    }

    protected fun updateUiState(function: (State) -> State) {
        _state.update(function)
    }

    protected suspend fun sendUiEvent(event: Event) {
        _event.send(event)
    }

}

interface IUiState
interface IUiEvent