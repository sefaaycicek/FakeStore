package com.baris.fakestore.domain.usecase

import com.baris.fakestore.core.IResult
import com.baris.fakestore.core.UseCase
import com.baris.fakestore.domain.model.ProductsResponse
import com.baris.fakestore.domain.repository.FakeStoreRepository
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Created on 26.02.2024.
 * @author saycicek
 */

@ViewModelScoped
class GetAllProductsUseCase @Inject constructor(
    private val repository: FakeStoreRepository
) : UseCase() {

    operator fun invoke(limit: Int, skip: Int): Flow<IResult<ProductsResponse>> {
        return execute { repository.getProducts(limit, skip) }
    }


}