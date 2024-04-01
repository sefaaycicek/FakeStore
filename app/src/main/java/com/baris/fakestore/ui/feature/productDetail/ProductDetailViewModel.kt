package com.baris.fakestore.ui.feature.productDetail

import androidx.lifecycle.viewModelScope
import com.baris.fakestore.core.BaseViewModel
import com.baris.fakestore.core.IUiEvent
import com.baris.fakestore.core.IUiState
import com.baris.fakestore.core.ResultError
import com.baris.fakestore.core.extensions.onError
import com.baris.fakestore.core.extensions.onLoading
import com.baris.fakestore.core.extensions.onSuccess
import com.baris.fakestore.domain.model.Product
import com.baris.fakestore.domain.usecase.AddFavoriteProductUseCase
import com.baris.fakestore.domain.usecase.AddToBasketUseCase
import com.baris.fakestore.domain.usecase.DeleteFavoriteProductUseCase
import com.baris.fakestore.domain.usecase.GetProductUseCase
import com.baris.fakestore.domain.usecase.UpdateBasketQuantityUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created on 27.02.2024.
 * @author saycicek
 */

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val getProductUseCase: GetProductUseCase,
    private val addToBasketUseCase: AddToBasketUseCase,
    private val updateBasketQuantityUseCase: UpdateBasketQuantityUseCase,
    private val addFavoriteProductUseCase: AddFavoriteProductUseCase,
    private val deleteFavoriteProductUseCase: DeleteFavoriteProductUseCase
) : BaseViewModel<ProductDetailViewModel.UiState, ProductDetailViewModel.UiEvent>() {

    fun getProductById(productId: Int) {
        viewModelScope.launch {
            getProductUseCase(productId).collect { result ->
                result.onLoading {
                    updateUiState(
                        UiState(
                            isLoading = true
                        )
                    )
                }.onSuccess { response ->
                    updateUiState(
                        UiState(
                            data = response
                        )
                    )
                }.onError { error ->
                    updateUiState(
                        UiState(
                            error = error
                        )
                    )
                }
            }
        }
    }

    fun addToCart() {
        viewModelScope.launch {
            state.value.data?.let { product ->
                val useCaseFlow = if (product.basketQuantity == 0)
                    addToBasketUseCase(product.id)
                else
                    updateBasketQuantityUseCase(product.id, product.basketQuantity + 1)

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
                                data = product.copy(
                                    basketQuantity = product.basketQuantity + 1
                                )
                            )
                        }
                        sendUiEvent(
                            UiEvent.ShowToast("Successfully Added")
                        )
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
    }

    fun onFavoriteClick() {
        viewModelScope.launch {
            state.value.data?.let { product ->
                val useCaseFlow = if (product.isFavorite)
                    deleteFavoriteProductUseCase(product.id)
                else addFavoriteProductUseCase(product.id)

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
                                data = product.copy(
                                    isFavorite = !product.isFavorite
                                )
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
    }

    data class UiState(
        val isLoading: Boolean = false,
        val data: Product? = null,
        val error: ResultError? = null
    ) : IUiState

    sealed interface UiEvent : IUiEvent {
        data object Init: UiEvent
        data class ShowToast(val message: String) : UiEvent
    }

    override fun initialState(): UiState {
        return UiState()
    }
}