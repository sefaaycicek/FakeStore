package com.baris.fakestore.ui.feature.favorites

import androidx.lifecycle.viewModelScope
import com.baris.fakestore.core.BaseViewModel
import com.baris.fakestore.core.IUiEvent
import com.baris.fakestore.core.IUiState
import com.baris.fakestore.core.ResultError
import com.baris.fakestore.core.extensions.onError
import com.baris.fakestore.core.extensions.onLoading
import com.baris.fakestore.core.extensions.onSuccess
import com.baris.fakestore.domain.model.Product
import com.baris.fakestore.domain.usecase.AddToBasketUseCase
import com.baris.fakestore.domain.usecase.DeleteFavoriteProductUseCase
import com.baris.fakestore.domain.usecase.GetFavoriteProductsUseCase
import com.baris.fakestore.domain.usecase.UpdateBasketQuantityUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created on 28.02.2024.
 * @author saycicek
 */
@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val getFavoriteProductsUseCase: GetFavoriteProductsUseCase,
    private val deleteFavoriteProductUseCase: DeleteFavoriteProductUseCase,
    private val addToBasketUseCase: AddToBasketUseCase,
    private val updateBasketQuantityUseCase: UpdateBasketQuantityUseCase
) : BaseViewModel<FavoriteViewModel.UiState, IUiEvent>() {

    fun getFavorites() {
        viewModelScope.launch {
            getFavoriteProductsUseCase().collect { result ->
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

    fun removeFavorite(productId: Int) {
        viewModelScope.launch {
            deleteFavoriteProductUseCase(productId).collect { result ->
                result.onLoading {
                    updateUiState {
                        it.copy(
                            isLoading = true
                        )
                    }
                }.onSuccess {
                    getFavorites()
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
            val useCaseFlow = if (quantity == 0)
                addToBasketUseCase(productId)
            else updateBasketQuantityUseCase(productId, quantity + 1)

            useCaseFlow.collect { result ->
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

    data class UiState(
        val isLoading: Boolean = false,
        val data: List<Product> = listOf(),
        val error: ResultError? = null
    ) : IUiState

    override fun initialState(): UiState {
        return UiState()
    }

}