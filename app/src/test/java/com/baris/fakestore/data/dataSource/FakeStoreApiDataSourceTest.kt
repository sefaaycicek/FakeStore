package com.baris.fakestore.data.dataSource

import com.baris.fakestore.data.model.ProductDto
import com.baris.fakestore.data.model.ProductsResponseDto
import com.baris.fakestore.data.source.api.FakeStoreApi
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

/**
 * Created on 28.02.2024.
 * @author saycicek
 */
class FakeStoreApiDataSourceTest {

    private lateinit var fakeStoreApi: FakeStoreApi
    private lateinit var sut: FakeStoreApiDataSource

    private val productId = 1
    private val limit = 100
    private val skip = 0
    private val query = "apple"
    private val category = "smartphones"

    @Before
    fun setup() {
        fakeStoreApi = mockk(relaxed = true)
        sut = FakeStoreApiDataSourceImpl(fakeStoreApi)
    }

    @Test
    fun `invokes getAllProducts fun in FakeStoreApi when invoke the getAllProducts fun`() =
        runTest {
            // when
            sut.getAllProducts(limit, skip)

            // then
            coVerify(exactly = 1) { fakeStoreApi.getAllProducts(any(), any()) }
        }

    @Test
    fun `returns ProductResponseDto when invoke the getAllProducts fun`() = runTest {

        val productsResponse: ProductsResponseDto = mockk(relaxed = true)
        // when
        coEvery { fakeStoreApi.getAllProducts(any<Int>(), any<Int>()) } returns productsResponse
        val result = sut.getAllProducts(limit, skip)

        // then
        assertEquals(result, productsResponse)
    }

    @Test
    fun `invokes getProductById fun in FakeStoreApi when invoke the getProductById fun`() =
        runTest {
            // when
            sut.getProductById(productId)

            // then
            coVerify(exactly = 1) { fakeStoreApi.getProductById(any()) }
        }

    @Test
    fun `returns ProductDto when invoke the getProductById fun`() = runTest {

        val product: ProductDto = mockk(relaxed = true)
        // when
        coEvery { fakeStoreApi.getProductById(any<Int>()) } returns product
        val result = sut.getProductById(productId)

        // then
        assertEquals(result, product)
    }

    @Test
    fun `invokes search fun in FakeStoreApi when invoke the search fun`() = runTest {
        // when
        sut.search(query, limit, skip)

        // then
        coVerify(exactly = 1) { fakeStoreApi.search(any(), any(), any()) }
    }

    @Test
    fun `returns ProductsResponseDto when invoke the search fun`() = runTest {

        val productsResponse: ProductsResponseDto = mockk(relaxed = true)
        // when
        coEvery { fakeStoreApi.search(any<String>(), any<Int>(), any<Int>()) } returns productsResponse
        val result = sut.search(query, limit, skip)

        // then
        assertEquals(result, productsResponse)
    }

    @Test
    fun `invokes getCategories fun in FakeStoreApi when invoke the getCategories fun`() = runTest {
        // when
        sut.getCategories()

        // then
        coVerify(exactly = 1) { fakeStoreApi.getCategories() }
    }

    @Test
    fun `returns Category List when invoke the getCategories fun`() = runTest {

        val categories: List<String> = mockk(relaxed = true)
        // when
        coEvery { fakeStoreApi.getCategories() } returns categories
        val result = sut.getCategories()

        // then
        assertEquals(result, categories)
    }

    @Test
    fun `invokes getProductsOfCategory fun in FakeStoreApi when invoke the getProductsOfCategory fun`() = runTest {
        // when
        sut.getProductsOfCategory(category)

        // then
        coVerify(exactly = 1) { fakeStoreApi.getProductsOfCategory(any()) }
    }

    @Test
    fun `returns ProductsResponseDto when invoke the getProductsOfCategory fun`() = runTest {

        val productsResponse: ProductsResponseDto = mockk(relaxed = true)
        // when
        coEvery { fakeStoreApi.getProductsOfCategory(any<String>()) } returns productsResponse
        val result = sut.getProductsOfCategory(category)

        // then
        assertEquals(result, productsResponse)
    }

}