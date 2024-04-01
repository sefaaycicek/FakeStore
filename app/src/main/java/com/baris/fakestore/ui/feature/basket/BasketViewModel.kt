package com.baris.fakestore.ui.feature.basket

import androidx.lifecycle.viewModelScope
import com.baris.fakestore.core.BaseViewModel
import com.baris.fakestore.core.IUiEvent
import com.baris.fakestore.core.IUiState
import com.baris.fakestore.core.ResultError
import com.baris.fakestore.core.extensions.onError
import com.baris.fakestore.core.extensions.onLoading
import com.baris.fakestore.core.extensions.onSuccess
import com.baris.fakestore.domain.model.Product
import com.baris.fakestore.domain.usecase.DeleteProductFromBasketUseCase
import com.baris.fakestore.domain.usecase.GetBasketProductsUseCase
import com.baris.fakestore.domain.usecase.UpdateBasketQuantityUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created on 28.02.2024.
 * @author saycicek
 */

@HiltViewModel
class BasketViewModel @Inject constructor(
    private val getBasketProductsUseCase: GetBasketProductsUseCase,
    private val updateBasketQuantityUseCase: UpdateBasketQuantityUseCase,
    private val deleteProductFromBasketUseCase: DeleteProductFromBasketUseCase
): BaseViewModel<BasketViewModel.UiState, IUiEvent>() {

    fun getBasketProducts() {
        viewModelScope.launch {
            getBasketProductsUseCase().collect { result ->
                result.onLoading {
                    updateUiState {
                        it.copy(
                            isLoading = true
                        )
                    }
                }.onSuccess { response ->
                    updateUiState {
                        it.copy(
                            isLoading = false,
                            data = response
                        )
                    }
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

    fun onIncreaseBasketQuantity(productId: Int, quantity: Int) {
        viewModelScope.launch {
            updateBasketQuantityUseCase(productId, quantity + 1).collect { result ->
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
                            data = it.data.map { product ->
                                if (product.id == productId)
                                    product.copy(
                                        basketQuantity = product.basketQuantity + 1
                                    )
                                else product
                            }
                        )
                    }
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

    fun onDecreaseBasketQuantity(productId: Int, quantity: Int) {
        viewModelScope.launch {
            updateBasketQuantityUseCase(productId, quantity - 1).collect { result ->
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
                            data = it.data.map { product ->
                                if (product.id == productId)
                                    product.copy(
                                        basketQuantity = product.basketQuantity - 1
                                    )
                                else product
                            }
                        )
                    }
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

    fun deleteBasketProduct(productId: Int) {
        viewModelScope.launch {
            deleteProductFromBasketUseCase(productId).collect { result ->
                result.onLoading {
                    updateUiState {
                        it.copy(
                            isLoading = true
                        )
                    }
                }.onSuccess {
                    getBasketProducts()
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
        val data: List<Product> = listOf(),
        val error: ResultError? = null
    ): IUiState

    override fun initialState(): UiState {
        return UiState()
    }
}