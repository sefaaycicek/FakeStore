package com.baris.fakestore.domain.repository

import com.baris.fakestore.domain.model.CategoryUiModel
import com.baris.fakestore.domain.model.Product
import com.baris.fakestore.domain.model.ProductsResponse

/**
 * Created on 26.02.2024.
 * @author saycicek
 */
interface FakeStoreRepository {

    suspend fun getProducts(
        limit: Int,
        skip: Int
    ): ProductsResponse

    suspend fun getProductById(
        id: Int
    ): Product

    suspend fun getCategories(): List<CategoryUiModel>

    suspend fun searchProduct(
        query: String,
        limit: Int,
        skip: Int
    ): ProductsResponse

    suspend fun getFavoriteProducts(): List<Product>

    suspend fun addFavoriteProduct(productId: Int)

    suspend fun deleteFavoriteProduct(productId: Int)

    suspend fun getBasketProducts(): List<Product>

    suspend fun addToBasket(productId: Int)

    suspend fun deleteProductFromBasket(productId: Int)

    suspend fun deleteBasket()

    suspend fun updateBasketQuantity(productId: Int, quantity: Int)

}