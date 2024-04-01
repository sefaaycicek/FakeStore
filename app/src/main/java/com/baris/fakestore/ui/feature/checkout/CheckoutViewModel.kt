package com.baris.fakestore.ui.feature.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.baris.fakestore.core.BaseViewModel
import com.baris.fakestore.core.IUiEvent
import com.baris.fakestore.core.IUiState
import com.baris.fakestore.core.ResultError
import com.baris.fakestore.core.extensions.onError
import com.baris.fakestore.core.extensions.onLoading
import com.baris.fakestore.core.extensions.onSuccess
import com.baris.fakestore.domain.usecase.DeleteBasketUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created on 28.02.2024.
 * @author saycicek
 */

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val deleteBasketUseCase: DeleteBasketUseCase
): BaseViewModel<CheckoutViewModel.UiState, CheckoutViewModel.UiEvent>() {

    private val creditCardRegex = Regex("^(?:4[0-9]{12}(?:[0-9]{3})?|[25][1-7][0-9]{14}|6(?:011|5[0-9][0-9])[0-9]{12}|3[47][0-9]{13}|3(?:0[0-5]|[68][0-9])[0-9]{11}|(?:2131|1800|35\\d{3})\\d{11})\$")
    private val nameRegex = Regex("^[a-zA-Z ,.'-]+\$")

    fun validateName(value: String) : Boolean {
        return value.trim().matches(nameRegex)
    }

    fun validateEmail(value: String) : Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(value).matches()
    }

    fun validateCreditCard(value: String) : Boolean {
        return value.trim().replace("-", "").matches(creditCardRegex)
    }

    fun completePayment() {
        viewModelScope.launch {
            deleteBasketUseCase().collect { result ->
                result.onLoading {
                    updateUiState {
                        it.copy(
                            isLoading = true
                        )
                    }
                }.onSuccess {
                    updateUiState {
                        it.copy(
                            isLoading = false,
                        )
                    }
                    sendUiEvent(UiEvent.Done)
                }.onError { error ->
                    updateUiState {
                        it.copy(
                            isLoading = false,
                            error = error
                        )
                    }
                }
            }
        }
    }

    data class UiState(
        val isLoading: Boolean = false,
        val error: ResultError? = null
    ): IUiState

    sealed interface UiEvent: IUiEvent {
        data object Init: UiEvent
        data object Done: UiEvent
    }

    override fun initialState(): UiState {
        return UiState()
    }
}