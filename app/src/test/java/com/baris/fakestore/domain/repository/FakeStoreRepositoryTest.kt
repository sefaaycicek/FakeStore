package com.baris.fakestore.domain.repository

import com.baris.fakestore.data.dataSource.FakeStoreApiDataSource
import com.baris.fakestore.data.dataSource.FakeStoreDatabaseDataSource
import com.baris.fakestore.data.mapper.toDomain
import com.baris.fakestore.data.model.ProductsResponseDto
import com.baris.fakestore.data.repository.FakeStoreRepositoryImpl
import com.baris.fakestore.data.source.local.entity.BasketProductEntity
import com.baris.fakestore.data.source.local.entity.FavoriteProductEntity
import com.baris.fakestore.di.IoDispatcher
import com.baris.fakestore.domain.model.CategoryUiModel
import com.baris.fakestore.domain.model.Product
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

/**
 * Created on 28.02.2024.
 * @author saycicek
 */
class FakeStoreRepositoryTest {

    private lateinit var sut: FakeStoreRepository
    private lateinit var apiDataSource: FakeStoreApiDataSource
    private lateinit var databaseDataSource: FakeStoreDatabaseDataSource

    @IoDispatcher
    private lateinit var ioDispatcher: CoroutineDispatcher

    private val limit = 20
    private val skip = 0
    private val productId = 1
    private val query = "apple"

    @Before
    fun setup() {
        apiDataSource = mockk(relaxed = true)
        databaseDataSource = mockk(relaxed = true)
        ioDispatcher = Dispatchers.IO
        sut = FakeStoreRepositoryImpl(apiDataSource, databaseDataSource, ioDispatcher)
    }

    @Test
    fun `invokes getAllProducts fun in FakeStoreApiDataSource when invoke the getProducts fun`() =
        runTest {
            // when
            sut.getProducts(limit, skip)

            // then
            coVerify(exactly = 1) { apiDataSource.getAllProducts(any(), any()) }
        }

    @Test
    fun `returns ProductsResponse when invoke the getProducts fun`() = runTest {

        val productResponse: ProductsResponseDto = mockk(relaxed = true)
        // when
        coEvery { apiDataSource.getAllProducts(any<Int>(), any<Int>()) } returns productResponse
        val result = sut.getProducts(limit, skip)

        // then
        Assert.assertEquals(result, productResponse.toDomain())
    }

    @Test
    fun `invokes search fun in FakeStoreApiDataSource when invoke the searchProduct fun`() =
        runTest {
            // when
            sut.searchProduct(query, limit, skip)

            // then
            coVerify(exactly = 1) { apiDataSource.search(any(), any(), any()) }
        }

    @Test
    fun `returns ProductsResponse when invoke the searchProduct fun`() = runTest {

        val productResponse: ProductsResponseDto = mockk(relaxed = true)
        // when
        coEvery {
            apiDataSource.search(
                any<String>(),
                any<Int>(),
                any<Int>()
            )
        } returns productResponse
        val result = sut.searchProduct(query, limit, skip)

        // then
        Assert.assertEquals(result, productResponse.toDomain())
    }

    @Test
    fun `invokes getProductById fun in FakeStoreApiDataSource and getBasketProducts fun, getFavoriteProducts fun in FakeStoreDatabaseDataSource when invoke the getFavoriteProducts fun`() =
        runTest {
            // when
            sut.getFavoriteProducts()
            val favoriteProducts = databaseDataSource.getFavoriteProducts()

            // then
            coVerify(exactly = favoriteProducts.size) { apiDataSource.getProductById(any()) }
            coVerify(exactly = 2) { databaseDataSource.getFavoriteProducts() }
            coVerify(exactly = 1) { databaseDataSource.getBasketProducts() }
        }

    @Test
    fun `returns Product List when invoke the getFavoriteProducts fun`() = runTest {
        val deferredProducts = mutableListOf<Deferred<Product>>()
        val favoriteProductIds = databaseDataSource.getFavoriteProducts().map { it.productId }
        val basketProducts = databaseDataSource.getBasketProducts()

        // when
        coEvery { apiDataSource.getProductById(any<Int>()) } returns mockk(relaxed = true)

        favoriteProductIds.forEach { id ->
            deferredProducts.add(
                async {
                    apiDataSource.getProductById(id).toDomain(
                        basketQuantity = basketProducts.find { it.productId == id }?.quantity ?: 0,
                        isFavorite = true
                    )
                }
            )
        }

        val result = sut.getFavoriteProducts()

        // then
        Assert.assertEquals(result, deferredProducts.awaitAll())
    }

    @Test
    fun `invokes addFavoriteProduct fun in FakeStoreDatabaseDataSource when invoke the addFavoriteProduct fun`() =
        runTest {
            // when
            sut.addFavoriteProduct(productId)

            // then
            coVerify(exactly = 1) { databaseDataSource.addFavoriteProduct(any()) }
        }

    @Test
    fun `returns Unit when invoke the addFavoriteProduct fun`() = runTest {
        // when
        coEvery { databaseDataSource.addFavoriteProduct(any<FavoriteProductEntity>()) } returns Unit
        val result = sut.addFavoriteProduct(productId)

        // then
        Assert.assertEquals(result, Unit)
    }

    @Test
    fun `invokes deleteFavoriteProduct fun in FakeStoreDatabaseDataSource when invoke the deleteFavoriteProduct fun`() =
        runTest {
            // when
            sut.deleteFavoriteProduct(productId)

            // then
            coVerify(exactly = 1) { databaseDataSource.deleteFavoriteProduct(any()) }
        }

    @Test
    fun `returns Unit when invoke the deleteFavoriteProduct fun`() = runTest {
        // when
        coEvery { databaseDataSource.deleteFavoriteProduct(any<Int>()) } returns Unit
        val result = sut.deleteFavoriteProduct(productId)

        // then
        Assert.assertEquals(result, Unit)
    }


    @Test
    fun `invokes getProductById fun in FakeStoreApiDataSource and getBasketProducts fun, getFavoriteProducts fun in FakeStoreDatabaseDataSource when invoke the getBasketProducts fun`() =
        runTest {
            // when
            sut.getBasketProducts()
            val basketProducts = databaseDataSource.getBasketProducts()

            // then
            coVerify(exactly = basketProducts.size) { apiDataSource.getProductById(any()) }
            coVerify(exactly = 1) { databaseDataSource.getFavoriteProducts() }
            coVerify(exactly = 2) { databaseDataSource.getBasketProducts() }
        }

    @Test
    fun `returns Product List when invoke the getBasketProducts fun`() = runTest {
        val deferredProducts = mutableListOf<Deferred<Product>>()
        val favoriteProductIds = databaseDataSource.getFavoriteProducts().map { it.productId }
        val basketProducts = databaseDataSource.getBasketProducts()

        // when
        coEvery { apiDataSource.getProductById(any<Int>()) } returns mockk(relaxed = true)

        basketProducts.forEach { basketProduct ->
            deferredProducts.add(
                async {
                    apiDataSource.getProductById(basketProduct.productId).toDomain(
                        basketQuantity = basketProduct.quantity,
                        isFavorite = favoriteProductIds.contains(basketProduct.productId)
                    )
                }
            )
        }

        val result = sut.getFavoriteProducts()

        // then
        Assert.assertEquals(result, deferredProducts.awaitAll())
    }

    @Test
    fun `invokes addBasketProduct fun in FakeStoreDatabaseDataSource when invoke the addToBasket fun`() =
        runTest {
            // when
            sut.addToBasket(productId)

            // then
            coVerify(exactly = 1) { databaseDataSource.addBasketProduct(any()) }
        }

    @Test
    fun `returns Unit when invoke the addToBasket fun`() = runTest {
        // when
        coEvery { databaseDataSource.addBasketProduct(any<BasketProductEntity>()) } returns Unit
        val result = sut.addToBasket(productId)

        // then
        Assert.assertEquals(result, Unit)
    }

    @Test
    fun `invokes deleteBasketProduct fun in FakeStoreDatabaseDataSource when invoke the deleteProductFromBasket fun`() =
        runTest {
            // when
            sut.deleteProductFromBasket(productId)

            // then
            coVerify(exactly = 1) { databaseDataSource.deleteBasketProduct(any()) }
        }

    @Test
    fun `returns Unit when invoke the deleteProductFromBasket fun`() = runTest {
        // when
        coEvery { databaseDataSource.deleteBasketProduct(any<Int>()) } returns Unit
        val result = sut.deleteProductFromBasket(productId)

        // then
        Assert.assertEquals(result, Unit)
    }

    @Test
    fun `invokes deleteBasket fun in FakeStoreDatabaseDataSource when invoke the deleteBasket fun`() =
        runTest {
            // when
            sut.deleteBasket()

            // then
            coVerify(exactly = 1) { databaseDataSource.deleteBasket() }
        }

    @Test
    fun `returns Unit when invoke the deleteBasket fun`() = runTest {
        // when
        coEvery { databaseDataSource.deleteBasket() } returns Unit
        val result = sut.deleteBasket()

        // then
        Assert.assertEquals(result, Unit)
    }

    @Test
    fun `invokes deleteBasketProduct fun in FakeStoreDatabaseDataSource when quantity is 0 when invoke the updateBasketQuantity fun`() =
        runTest {
            // when
            sut.updateBasketQuantity(productId, 0)

            // then
            coVerify(exactly = 1) { databaseDataSource.deleteBasketProduct(any()) }
        }

    @Test
    fun `invokes updateBasketProductQuantity fun in FakeStoreDatabaseDataSource when quantity is higher than 0 when invoke the updateBasketQuantity fun`() =
        runTest {
            // when
            sut.updateBasketQuantity(productId, 1)

            // then
            coVerify(exactly = 1) { databaseDataSource.updateBasketProductQuantity(any()) }
        }

    @Test
    fun `returns Unit when quantity is 0 when invoke the updateBasketQuantity fun`() = runTest {
        // when
        coEvery { databaseDataSource.deleteBasketProduct(any<Int>()) } returns Unit
        val result = sut.updateBasketQuantity(productId, 0)

        // then
        Assert.assertEquals(result, Unit)
    }

    @Test
    fun `returns Unit when quantity is higher than 0 when invoke the updateBasketQuantity fun`() =
        runTest {
            // when
            coEvery { databaseDataSource.updateBasketProductQuantity(any<BasketProductEntity>()) } returns Unit
            val result = sut.updateBasketQuantity(productId, 1)

            // then
            Assert.assertEquals(result, Unit)
        }

    @Test
    fun `invokes getCategories fun in FakeStoreApiDataSource when invoke the getCategories fun`() =
        runTest {
            // when
            sut.getCategories()

            // then
            coVerify(exactly = 1) { apiDataSource.getCategories() }
        }

    @Test
    fun `returns String List when invoke the getCategories fun`() = runTest {
        val mockResponse: List<String> = listOf("")
        // when
        coEvery { apiDataSource.getCategories() } returns mockResponse
        val result = sut.getCategories()

        // then
        Assert.assertEquals(result, mockResponse.map {CategoryUiModel(it) })
    }

}