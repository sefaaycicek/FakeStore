package com.baris.fakestore.data.dataSource

import com.baris.fakestore.data.model.ProductDto
import com.baris.fakestore.data.model.ProductsResponseDto

/**
 * Created on 26.02.2024.
 * @author saycicek
 */
interface FakeStoreApiDataSource {

    suspend fun getAllProducts(
        limit: Int,
        skip: Int
    ): ProductsResponseDto

    suspend fun getProductById(
        id: Int
    ): ProductDto

    suspend fun search(
        query: String,
        limit: Int,
        skip: Int
    ): ProductsResponseDto

    suspend fun getCategories(): List<String>

    suspend fun getProductsOfCategory(
        category: String
    ): ProductsResponseDto

}