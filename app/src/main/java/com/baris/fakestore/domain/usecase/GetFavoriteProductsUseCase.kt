package com.baris.fakestore.domain.usecase

import com.baris.fakestore.core.IResult
import com.baris.fakestore.core.UseCase
import com.baris.fakestore.domain.model.Product
import com.baris.fakestore.domain.repository.FakeStoreRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Created on 26.02.2024.
 * @author saycicek
 */
class GetFavoriteProductsUseCase @Inject constructor(
    private val repository: FakeStoreRepository
) : UseCase() {

    operator fun invoke(): Flow<IResult<List<Product>>> {
        return execute { repository.getFavoriteProducts() }
    }

}