package com.baris.fakestore.ui.feature.filter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.baris.fakestore.core.BaseViewModel
import com.baris.fakestore.core.IUiEvent
import com.baris.fakestore.core.IUiState
import com.baris.fakestore.core.ResultError
import com.baris.fakestore.core.extensions.onError
import com.baris.fakestore.core.extensions.onLoading
import com.baris.fakestore.core.extensions.onSuccess
import com.baris.fakestore.domain.model.CategoryUiModel
import com.baris.fakestore.domain.usecase.GetCategoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created on 29.02.2024.
 * @author saycicek
 */

@HiltViewModel
class FilterViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase
): BaseViewModel<FilterViewModel.UiState, IUiEvent>() {

    var selectedCategoriesResult = listOf<String>()

    fun getCategories() {
        viewModelScope.launch {
            getCategoriesUseCase().collect { result ->
                result.onLoading {
                    updateUiState {
                        it.copy(
                            isLoading = true
                        )
                    }
                }.onSuccess { list ->
                    updateUiState {
                        it.copy(
                            isLoading = false,
                            data = selectCategories(list)
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

    fun selectCategory(category: String, isSelected: Boolean) {
        val newList = state.value.data.map {
            if(it.category == category)
                it.copy(
                    isSelected = isSelected
                )
            else it
        }
        updateUiState {
            it.copy(
                data = newList
            )
        }
    }

    private fun selectCategories(categories: List<CategoryUiModel>): List<CategoryUiModel> {
        val newList = categories.map {
            if(selectedCategoriesResult.contains(it.category))
                it.copy(
                    isSelected = true
                )
            else it
        }
        return newList
    }

    data class UiState(
        val isLoading: Boolean = false,
        val data: List<CategoryUiModel> = listOf(),
        val error: ResultError? = null
    ): IUiState

    override fun initialState(): UiState {
        return UiState()
    }

}