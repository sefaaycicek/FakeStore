package com.baris.fakestore.data.repository

import com.baris.fakestore.data.dataSource.FakeStoreApiDataSource
import com.baris.fakestore.data.dataSource.FakeStoreDatabaseDataSource
import com.baris.fakestore.data.mapper.toDomain
import com.baris.fakestore.data.source.local.entity.BasketProductEntity
import com.baris.fakestore.data.source.local.entity.FavoriteProductEntity
import com.baris.fakestore.di.IoDispatcher
import com.baris.fakestore.domain.model.CategoryUiModel
import com.baris.fakestore.domain.model.Product
import com.baris.fakestore.domain.model.ProductsResponse
import com.baris.fakestore.domain.repository.FakeStoreRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject

/**
 * Created on 26.02.2024.
 * @author saycicek
 */
class FakeStoreRepositoryImpl @Inject constructor(
    private val apiDataSource: FakeStoreApiDataSource,
    private val databaseDataSource: FakeStoreDatabaseDataSource,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : FakeStoreRepository {
    override suspend fun getProducts(limit: Int, skip: Int): ProductsResponse =
        withContext(dispatcher) {
            apiDataSource.getAllProducts(limit, skip).toDomain()
        }

    override suspend fun getProductById(id: Int): Product = withContext(dispatcher) {
        val favoriteProductsIds = databaseDataSource.getFavoriteProductById(id)
        val basketProduct = databaseDataSource.getBasketProductById(id)

        apiDataSource.getProductById(id).toDomain(
            basketQuantity = basketProduct?.quantity ?: 0,
            isFavorite = favoriteProductsIds != null
        )
    }

    override suspend fun getCategories(): List<CategoryUiModel> = withContext(dispatcher) {
        apiDataSource.getCategories().map {
            CategoryUiModel(
                category = it
            )
        }
    }

    override suspend fun searchProduct(query: String, limit: Int, skip: Int): ProductsResponse =
        withContext(dispatcher) {
            apiDataSource.search(query, limit, skip).toDomain()
        }

    override suspend fun getFavoriteProducts(): List<Product> = withContext(dispatcher) {
        val deferredProducts = mutableListOf<Deferred<Product>>()
        val basketProducts = databaseDataSource.getBasketProducts()
        val favoriteProductsIds = databaseDataSource.getFavoriteProducts().map { it.productId }

        favoriteProductsIds.forEach { id ->
            deferredProducts.add(
                async {
                    apiDataSource.getProductById(id).toDomain(
                        basketQuantity = basketProducts.find { it.productId == id }?.quantity ?: 0,
                        isFavorite = true
                    )
                }
            )
        }
        deferredProducts.awaitAll()
    }

    override suspend fun addFavoriteProduct(productId: Int) = withContext(dispatcher) {
        databaseDataSource.addFavoriteProduct(FavoriteProductEntity(productId))
    }

    override suspend fun deleteFavoriteProduct(productId: Int) = withContext(dispatcher) {
        databaseDataSource.deleteFavoriteProduct(productId)
    }

    override suspend fun getBasketProducts(): List<Product> = withContext(dispatcher) {
        val deferredProducts = mutableListOf<Deferred<Product>>()
        val basketProducts = databaseDataSource.getBasketProducts()
        val favoriteProductsIds = databaseDataSource.getFavoriteProducts().map { it.productId }

        basketProducts.forEach { basketProduct ->
            deferredProducts.add(
                async {
                    apiDataSource.getProductById(basketProduct.productId).toDomain(
                        basketQuantity = basketProduct.quantity,
                        isFavorite = favoriteProductsIds.contains(basketProduct.productId)
                    )
                }
            )
        }
        deferredProducts.awaitAll()
    }

    override suspend fun addToBasket(productId: Int) = withContext(dispatcher) {
        databaseDataSource.addBasketProduct(BasketProductEntity(productId, 1))
    }

    override suspend fun deleteProductFromBasket(productId: Int) = withContext(dispatcher) {
        databaseDataSource.deleteBasketProduct(productId)
    }

    override suspend fun deleteBasket() = withContext(dispatcher) {
        databaseDataSource.deleteBasket()
    }

    override suspend fun updateBasketQuantity(productId: Int, quantity: Int) {
        if (quantity == 0) {
            databaseDataSource.deleteBasketProduct(productId)
        } else {
            databaseDataSource.updateBasketProductQuantity(BasketProductEntity(productId, quantity))
        }
    }
}