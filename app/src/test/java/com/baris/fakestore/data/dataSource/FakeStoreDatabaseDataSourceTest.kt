package com.baris.fakestore.data.dataSource

import com.baris.fakestore.data.source.local.dao.BasketProductsDao
import com.baris.fakestore.data.source.local.dao.FavoriteProductsDao
import com.baris.fakestore.data.source.local.entity.BasketProductEntity
import com.baris.fakestore.data.source.local.entity.FavoriteProductEntity
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

/**
 * Created on 28.02.2024.
 * @author saycicek
 */
class FakeStoreDatabaseDataSourceTest {

    private lateinit var sut: FakeStoreDatabaseDataSource
    private lateinit var basketProductsDao: BasketProductsDao
    private lateinit var favoriteProductsDao: FavoriteProductsDao

    private val productId = 1

    @Before
    fun setup() {
        basketProductsDao = mockk(relaxed = true)
        favoriteProductsDao = mockk(relaxed = true)
        sut = FakeStoreDatabaseDataSourceImpl(basketProductsDao, favoriteProductsDao)
    }

    @Test
    fun `invokes getBasketProducts fun in BasketProductsDao when invoke the getBasketProducts fun`() =
        runTest {
            // when
            sut.getBasketProducts()

            // then
            coVerify(exactly = 1) { basketProductsDao.getBasketProducts() }
        }

    @Test
    fun `returns BasketProductEntity List when invoke the getBasketProducts fun`() = runTest {

        val entities: List<BasketProductEntity> = mockk(relaxed = true)
        // when
        coEvery { basketProductsDao.getBasketProducts() } returns entities
        val result = sut.getBasketProducts()

        // then
        Assert.assertEquals(result, entities)
    }

    @Test
    fun `invokes getBasketProductById fun in BasketProductsDao when invoke the getBasketProductById fun`() =
        runTest {
            // when
            sut.getBasketProductById(productId)

            // then
            coVerify(exactly = 1) { basketProductsDao.getBasketProductById(any()) }
        }

    @Test
    fun `returns BasketProductEntity List when invoke the getBasketProductById fun`() = runTest {

        val entity: BasketProductEntity = mockk(relaxed = true)
        // when
        coEvery { basketProductsDao.getBasketProductById(any<Int>()) } returns entity
        val result = sut.getBasketProductById(productId)

        // then
        Assert.assertEquals(result, entity)
    }

    @Test
    fun `returns null when no product with that id when invoke the getBasketProductById fun`() = runTest {

        // when
        coEvery { basketProductsDao.getBasketProductById(any<Int>()) } returns null
        val result = sut.getBasketProductById(productId)

        // then
        Assert.assertEquals(result, null)
    }

    @Test
    fun `invokes addBasketProduct fun in BasketProductsDao when invoke the addBasketProduct fun`() =
        runTest {
            val entity: BasketProductEntity = mockk(relaxed = true)
            // when
            sut.addBasketProduct(entity)

            // then
            coVerify(exactly = 1) { basketProductsDao.addBasketProduct(any()) }
        }

    @Test
    fun `returns Unit when invoke the addBasketProduct fun`() = runTest {

        val entity: BasketProductEntity = mockk(relaxed = true)
        // when
        coEvery { basketProductsDao.addBasketProduct(any<BasketProductEntity>()) } returns Unit
        val result = sut.addBasketProduct(entity)

        // then
        Assert.assertEquals(result, Unit)
    }

    @Test
    fun `invokes deleteBasketProductById fun in BasketProductsDao when invoke the deleteBasketProduct fun`() =
        runTest {
            // when
            sut.deleteBasketProduct(productId)

            // then
            coVerify(exactly = 1) { basketProductsDao.deleteBasketProductById(any()) }
        }

    @Test
    fun `returns Unit when invoke the deleteBasketProduct fun`() = runTest {
        // when
        coEvery { basketProductsDao.deleteBasketProductById(any<Int>()) } returns Unit
        val result = sut.deleteBasketProduct(productId)

        // then
        Assert.assertEquals(result, Unit)
    }

    @Test
    fun `invokes deleteBasket fun in BasketProductsDao when invoke the deleteBasket fun`() =
        runTest {
            // when
            sut.deleteBasket()

            // then
            coVerify(exactly = 1) { basketProductsDao.deleteBasket() }
        }

    @Test
    fun `returns Unit when invoke the deleteBasket fun`() = runTest {
        // when
        coEvery { basketProductsDao.deleteBasket() } returns Unit
        val result = sut.deleteBasket()

        // then
        Assert.assertEquals(result, Unit)
    }

    @Test
    fun `invokes updateBasketProductQuantity fun in BasketProductsDao when invoke the updateBasketProductQuantity fun`() =
        runTest {
            val entity: BasketProductEntity = mockk(relaxed = true)
            // when
            sut.updateBasketProductQuantity(entity)

            // then
            coVerify(exactly = 1) { basketProductsDao.updateBasketProductQuantity(any()) }
        }

    @Test
    fun `returns Unit when invoke the updateBasketProductQuantity fun`() = runTest {

        val entity: BasketProductEntity = mockk(relaxed = true)
        // when
        coEvery { basketProductsDao.updateBasketProductQuantity(any<BasketProductEntity>()) } returns Unit
        val result = sut.updateBasketProductQuantity(entity)

        // then
        Assert.assertEquals(result, Unit)
    }

    @Test
    fun `invokes getFavoriteProducts fun in FavoriteProductsDao when invoke the getFavoriteProducts fun`() =
        runTest {
            // when
            sut.getFavoriteProducts()

            // then
            coVerify(exactly = 1) { favoriteProductsDao.getFavoriteProducts() }
        }

    @Test
    fun `returns FavoriteProductEntity List when invoke the getFavoriteProducts fun`() = runTest {
        val entities: List<FavoriteProductEntity> = mockk(relaxed = true)
        // when
        coEvery { favoriteProductsDao.getFavoriteProducts() } returns entities
        val result = sut.getFavoriteProducts()

        // then
        Assert.assertEquals(result, entities)
    }

    @Test
    fun `invokes getFavoriteProductById fun in FavoriteProductsDao when invoke the getFavoriteProductById fun`() =
        runTest {
            // when
            sut.getFavoriteProductById(productId)

            // then
            coVerify(exactly = 1) { favoriteProductsDao.getFavoriteProductById(any()) }
        }

    @Test
    fun `returns FavoriteProductEntity when invoke the getFavoriteProductById fun`() = runTest {
        val entity: FavoriteProductEntity = mockk(relaxed = true)
        // when
        coEvery { favoriteProductsDao.getFavoriteProductById(any<Int>()) } returns entity
        val result = sut.getFavoriteProductById(productId)

        // then
        Assert.assertEquals(result, entity)
    }

    @Test
    fun `returns null when no product with that id when invoke the getFavoriteProductById fun`() = runTest {
        // when
        coEvery { favoriteProductsDao.getFavoriteProductById(any<Int>()) } returns null
        val result = sut.getFavoriteProductById(productId)

        // then
        Assert.assertEquals(result, null)
    }

    @Test
    fun `invokes addFavoriteProduct fun in FavoriteProductsDao when invoke the addFavoriteProduct fun`() =
        runTest {
            val entity: FavoriteProductEntity = mockk(relaxed = true)
            // when
            sut.addFavoriteProduct(entity)

            // then
            coVerify(exactly = 1) { favoriteProductsDao.addFavoriteProduct(any()) }
        }

    @Test
    fun `returns Unit when invoke the addFavoriteProduct fun`() = runTest {
        val entity: FavoriteProductEntity = mockk(relaxed = true)
        // when
        coEvery { favoriteProductsDao.addFavoriteProduct(any<FavoriteProductEntity>()) } returns Unit
        val result = sut.addFavoriteProduct(entity)

        // then
        Assert.assertEquals(result, Unit)
    }

    @Test
    fun `invokes deleteFavoriteProductById fun in FavoriteProductsDao when invoke the deleteFavoriteProduct fun`() =
        runTest {
            // when
            sut.deleteFavoriteProduct(productId)

            // then
            coVerify(exactly = 1) { favoriteProductsDao.deleteFavoriteProductById(any()) }
        }

    @Test
    fun `returns Unit when invoke the deleteFavoriteProduct fun`() = runTest {
        // when
        coEvery { favoriteProductsDao.deleteFavoriteProductById(any<Int>()) } returns Unit
        val result = sut.deleteFavoriteProduct(productId)

        // then
        Assert.assertEquals(result, Unit)
    }
}