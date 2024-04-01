package com.baris.fakestore.domain.usecase

import com.baris.fakestore.core.IResult
import com.baris.fakestore.core.UseCase
import com.baris.fakestore.domain.model.CategoryUiModel
import com.baris.fakestore.domain.repository.FakeStoreRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Created on 28.02.2024.
 * @author saycicek
 */
class GetCategoriesUseCase @Inject constructor(
    private val repository: FakeStoreRepository
) : UseCase() {

    operator fun invoke(): Flow<IResult<List<CategoryUiModel>>> {
        return execute { repository.getCategories() }
    }

}