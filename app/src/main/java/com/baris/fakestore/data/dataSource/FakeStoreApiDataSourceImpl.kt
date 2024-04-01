package com.baris.fakestore.data.dataSource

import com.baris.fakestore.data.source.api.FakeStoreApi
import com.baris.fakestore.data.model.ProductDto
import com.baris.fakestore.data.model.ProductsResponseDto
import javax.inject.Inject

/**
 * Created on 26.02.2024.
 * @author saycicek
 */
class FakeStoreApiDataSourceImpl @Inject constructor(
    private val fakeStoreApi: FakeStoreApi
): FakeStoreApiDataSource {
    override suspend fun getAllProducts(limit: Int, skip: Int): ProductsResponseDto {
        return fakeStoreApi.getAllProducts(
            limit = limit,
            skip = skip
        )
    }

    override suspend fun getProductById(id: Int): ProductDto {
        return fakeStoreApi.getProductById(id)
    }

    override suspend fun search(query: String, limit: Int, skip: Int): ProductsResponseDto {
        return fakeStoreApi.search(query, limit, skip)
    }

    override suspend fun getCategories(): List<String> {
        return fakeStoreApi.getCategories()
    }

    override suspend fun getProductsOfCategory(category: String): ProductsResponseDto {
        return fakeStoreApi.getProductsOfCategory(category)
    }

}