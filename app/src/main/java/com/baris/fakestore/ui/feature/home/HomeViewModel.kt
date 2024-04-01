package com.baris.fakestore.ui.feature.home

import androidx.lifecycle.viewModelScope
import com.baris.fakestore.common.SortType
import com.baris.fakestore.core.BaseViewModel
import com.baris.fakestore.core.IUiEvent
import com.baris.fakestore.core.IUiState
import com.baris.fakestore.core.ResultError
import com.baris.fakestore.core.extensions.onError
import com.baris.fakestore.core.extensions.onLoading
import com.baris.fakestore.core.extensions.onSuccess
import com.baris.fakestore.domain.model.Product
import com.baris.fakestore.domain.model.SortModel
import com.baris.fakestore.domain.usecase.GetAllProductsUseCase
import com.baris.fakestore.domain.usecase.SearchProductUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created on 26.02.2024.
 * @author saycicek
 */

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllProductsUseCase: GetAllProductsUseCase,
    private val searchProductUseCase: SearchProductUseCase
) : BaseViewModel<HomeViewModel.UiState, IUiEvent>() {

    private var limit = 20
    private var skip = 0
    private val searchInterval = 1000L
    private var _lastSearchQuery: String = ""
    val lastSearchQuery get() = _lastSearchQuery

    var filterResult = FilterResultModel(null, null, listOf())

    private var searchJob: Job? = null

    private var products = mutableListOf<Product>()

    private var _sortModels = listOf(
        SortModel(
            sortType = SortType.RECOMMENDED,
            text = "Recommended",
            isSelected = true
        ),
        SortModel(
            sortType = SortType.A_Z,
            text = "A to Z",
            isSelected = false
        ),
        SortModel(
            sortType = SortType.Z_A,
            text = "Z to A",
            isSelected = false
        ),
        SortModel(
            sortType = SortType.PRICE_ASC,
            text = "Increasing by price",
            isSelected = false
        ),
        SortModel(
            sortType = SortType.PRICE_DESC,
            text = "Decreasing by price",
            isSelected = false
        )
    )
    val sortModels get() = _sortModels

    fun getProducts() {
        viewModelScope.launch {
            getAllProductsUseCase(limit, skip).collect { result ->
                result.onLoading {
                    updateUiState {
                        it.copy(
                            isLoading = true
                        )
                    }
                }.onSuccess { response ->
                    products.addAll(response.products)
                    val uiModel = UiModel(
                        total = response.total,
                        products = sortedBy(sortModels.find { it.isSelected }?.sortType)
                    )
                    updateUiState {
                        it.copy(
                            isLoading = false,
                            data = uiModel
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

    fun search(query: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            if (query.isEmpty()) {
                resetPagination()
                getProducts()
                _lastSearchQuery = ""
                return@launch
            }
            delay(searchInterval)
            if (isNewSearchProcess(query))
                resetPagination()
            searchProductUseCase(query, limit, skip).collect { result ->
                result.onLoading {
                    updateUiState {
                        it.copy(
                            isLoading = true
                        )
                    }
                }.onSuccess { response ->
                    _lastSearchQuery = query
                    products.addAll(response.products)
                    val uiModel = UiModel(
                        total = response.total,
                        products = filteredByResult().let { sortedBy(sortModels.find { it.isSelected }?.sortType) }
                    )
                    updateUiState {
                        it.copy(
                            isLoading = false,
                            data = uiModel
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

    private fun isNewSearchProcess(query: String): Boolean {
        return _lastSearchQuery != query
    }

    fun isLastPage(): Boolean {
        val total = state.value.data.total ?: 0
        return skip >= total || total <= limit
    }

    fun nextPage() {
        val total = state.value.data.total ?: 0
        if (skip >= total)
            return

        skip += 20

        if (_lastSearchQuery.isEmpty()) {
            getProducts()
        } else {
            search(_lastSearchQuery)
        }
    }

    private fun sortedBy(sortType: SortType?): List<Product> {
        val products = filteredByResult()

        return when (sortType) {
            SortType.RECOMMENDED -> products
            SortType.A_Z -> products.sortedBy { it.title }
            SortType.Z_A -> products.sortedBy { it.title }.reversed()
            SortType.PRICE_ASC -> products.sortedBy { it.newPrice }
            SortType.PRICE_DESC -> products.sortedBy { it.newPrice }.reversed()
            null -> products
        }
    }

    fun sortBy(sortType: SortType) {
        val products = filteredByResult()

        val newList = sortModels.map { item ->
            item.copy(
                isSelected = item.sortType == sortType
            )
        }
        _sortModels = newList

        val sortedList = when (sortType) {
            SortType.RECOMMENDED -> products
            SortType.A_Z -> products.sortedBy { it.title }
            SortType.Z_A -> products.sortedBy { it.title }.reversed()
            SortType.PRICE_ASC -> products.sortedBy { it.newPrice }
            SortType.PRICE_DESC -> products.sortedBy { it.newPrice }.reversed()
        }

        updateUiState {
            it.copy(
                data = it.data.copy(
                    products = sortedList
                )
            )
        }
    }

    private fun resetPagination() {
        skip = 0
        products.clear()
        resetFilters()
    }

    private fun resetFilters() {
        val newList = sortModels.mapIndexed { index, item ->
            item.copy(
                isSelected = index == 0
            )
        }
        _sortModels = newList
        filterResult = FilterResultModel(0.0, 0.0, listOf())
    }

    private fun filteredByResult(): List<Product> {
        return products.filter {
            if(filterResult.minPrice != null)
                it.newPrice >= filterResult.minPrice!!
            else true
        }.filter {
            if(filterResult.maxPrice != null)
                it.newPrice <= filterResult.maxPrice!!
            else true
        }.filter {
            if(filterResult.categories.isNotEmpty()) {
                filterResult.categories.contains(it.category)
            } else true
        }
    }

    fun updateProductsByFilter() {
        val newList = products.filter {
            if(filterResult.minPrice != null)
                it.newPrice >= filterResult.minPrice!!
            else true
        }.filter {
            if(filterResult.maxPrice != null)
                it.newPrice <= filterResult.maxPrice!!
            else true
        }.filter {
            if(filterResult.categories.isNotEmpty()) {
                filterResult.categories.contains(it.category)
            } else true
        }

        updateUiState {
            it.copy(
                data = it.data.copy(
                    products = newList
                )
            )
        }
    }

    data class UiState(
        val isLoading: Boolean = false,
        val data: UiModel = UiModel(),
        val error: ResultError? = null
    ) : IUiState

    data class UiModel(
        val products: List<Product> = listOf(),
        val sortList: List<SortModel> = listOf(),
        val total: Int? = null,
        val canPaginate: Boolean = false
    )

    override fun initialState(): UiState {
        return UiState()
    }

}
