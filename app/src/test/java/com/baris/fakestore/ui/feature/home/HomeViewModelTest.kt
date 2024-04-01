package com.baris.fakestore.ui.feature.home

import com.baris.fakestore.core.IResult
import com.baris.fakestore.core.ResultError
import com.baris.fakestore.domain.model.Product
import com.baris.fakestore.domain.model.ProductsResponse
import com.baris.fakestore.domain.usecase.GetAllProductsUseCase
import com.baris.fakestore.domain.usecase.SearchProductUseCase
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import kotlin.random.Random

/**
 * Created on 28.02.2024.
 * @author saycicek
 */
class HomeViewModelTest {

    private lateinit var sut: HomeViewModel
    private lateinit var getAllProductsUseCase: GetAllProductsUseCase
    private lateinit var searchProductUseCase: SearchProductUseCase

    @OptIn(ExperimentalCoroutinesApi::class)
    private val dispatcher = UnconfinedTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        getAllProductsUseCase = mockk(relaxed = true)
        searchProductUseCase = mockk(relaxed = true)
        sut = HomeViewModel(getAllProductsUseCase, searchProductUseCase)
        Dispatchers.setMain(dispatcher)
    }

    @Test
    fun `invokes getAllProductsUseCase when invoke the getProducts fun`() {
        // when
        sut.getProducts()
        //then
        coVerify(exactly = 1) { getAllProductsUseCase(any(), any()) }
    }

    @Test
    fun `getAllProductsUseCase returns flow and if collects IResult Loading then isLoading of UiState updates as true when invoke the getProducts fun`() {
        // when
        every { getAllProductsUseCase(any(), any()) } answers { flowOf(IResult.Loading) }

        sut.getProducts()

        // then
        assertTrue(sut.state.value.isLoading)
    }

    @Test
    fun `getAllProductsUseCase returns flow and if collects IResult Success then products of UiModel will be updated with returned product when invoke the getProducts fun`() {
        // given
        val mockList: List<Product> = listOf(mockk())
        val mockProductResponse = ProductsResponse(
            Random.nextInt(),
            mockList,
            Random.nextInt(),
            Random.nextInt()
        )

        // when
        every { getAllProductsUseCase(any(), any()) } answers {
            flowOf(
                IResult.Success(
                    mockProductResponse
                )
            )
        }

        sut.getProducts()

        // then
        assertTrue(sut.state.value.data.products == mockList)

    }

    @Test
    fun `getAllProductsUseCase returns flow and if collects IResult Success then isLoading of UiState will be updated as false when invoke the getProducts fun`() {
        // given
        val mockList: List<Product> = listOf(mockk())
        val mockProductResponse = ProductsResponse(
            Random.nextInt(),
            mockList,
            Random.nextInt(),
            Random.nextInt()
        )

        // when
        every { getAllProductsUseCase(any(), any()) } answers {
            flowOf(
                IResult.Success(
                    mockProductResponse
                )
            )
        }

        sut.getProducts()

        // then
        assertTrue(!sut.state.value.isLoading)

    }

    @Test
    fun `getAllProductsUseCase returns flow and if collects IResult Success then total of UiModel will be updated with returned total when invoke the getProducts fun`() {
        // given
        val mockTotal = Random.nextInt()
        val mockList: List<Product> = listOf(mockk())
        val mockProductResponse = ProductsResponse(
            Random.nextInt(),
            mockList,
            Random.nextInt(),
            mockTotal
        )

        // when
        every { getAllProductsUseCase(any(), any()) } answers {
            flowOf(
                IResult.Success(
                    mockProductResponse
                )
            )
        }

        sut.getProducts()

        // then
        assertTrue(sut.state.value.data.total == mockTotal)

    }

    @Test
    fun `getAllProductsUseCase returns flow and if collects IResult Error then error of UiState updates with ResultError when invoke the getProducts fun`() {
        // given
        val mockResultError = ResultError("")

        // when
        every {
            getAllProductsUseCase(
                any(),
                any()
            )
        } answers { flowOf(IResult.Error(mockResultError)) }
        sut.getProducts()

        // then
        assertTrue(sut.state.value.error == mockResultError)
    }

    @Test
    fun `getAllProductsUseCase returns flow and if collects IResult Error then isLoading of UiState updated as false when invoke the getProducts fun`() {
        // given
        val mockResultError = ResultError("")

        // when
        every {
            getAllProductsUseCase(
                any(),
                any()
            )
        } answers { flowOf(IResult.Error(mockResultError)) }
        sut.getProducts()

        // then
        assertTrue(!sut.state.value.isLoading)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `invokes searchProductUseCase when query is not empty when invoke the search fun`() =
        runTest {
            // when
            sut.search("asd")

            // then
            advanceUntilIdle()
            coVerify(exactly = 1) { searchProductUseCase(any(), any(), any()) }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `invokes getAllProductsUseCase when query is empty when invoke the search fun`() = runTest {
        // when
        sut.search("")

        // then
        advanceUntilIdle()
        coVerify(exactly = 1) { getAllProductsUseCase(any(), any()) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `searchProductUseCase returns flow and if collects IResult Loading then isLoading of UiState updates as true when invoke the search fun`() =
        runTest {
            // when
            every { searchProductUseCase(any(), any(), any()) } answers { flowOf(IResult.Loading) }

            sut.search("abc")

            // then
            advanceUntilIdle()
            assertTrue(sut.state.value.isLoading)
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `searchProductUseCase returns flow and if collects IResult Success then products of UiModel will be updated with returned product when invoke the search fun`() =
        runTest {
            // given
            val mockQuery = "abc"
            val mockList: List<Product> = listOf(mockk())
            val mockProductResponse = ProductsResponse(
                Random.nextInt(),
                mockList,
                Random.nextInt(),
                Random.nextInt()
            )

            // when
            every { searchProductUseCase(any(), any(), any()) } answers {
                flowOf(
                    IResult.Success(
                        mockProductResponse
                    )
                )
            }

            sut.search(mockQuery)
            advanceUntilIdle()


            // then
            assertTrue(sut.state.value.data.products == mockList)

        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `searchProductUseCase returns flow and if collects IResult Success then isLoading of UiState will be updated as false when invoke the search fun`() =
        runTest {
            // given
            val mockQuery = "abc"
            val mockList: List<Product> = listOf(mockk())
            val mockProductResponse = ProductsResponse(
                Random.nextInt(),
                mockList,
                Random.nextInt(),
                Random.nextInt()
            )

            // when
            every { searchProductUseCase(any(), any(), any()) } answers {
                flowOf(
                    IResult.Success(
                        mockProductResponse
                    )
                )
            }

            sut.search(mockQuery)
            advanceUntilIdle()

            // then
            assertTrue(!sut.state.value.isLoading)

        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `searchProductUseCase returns flow and if collects IResult Success then total of UiModel will be updated with returned total when invoke the search fun`() =
        runTest {
            // given
            val mockQuery = "abc"
            val mockTotal = Random.nextInt()
            val mockList: List<Product> = listOf(mockk())
            val mockProductResponse = ProductsResponse(
                Random.nextInt(),
                mockList,
                Random.nextInt(),
                mockTotal
            )

            // when
            every { searchProductUseCase(any(), any(), any()) } answers {
                flowOf(
                    IResult.Success(
                        mockProductResponse
                    )
                )
            }

            sut.search(mockQuery)
            advanceUntilIdle()

            // then
            assertTrue(sut.state.value.data.total == mockTotal)

        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `searchProductUseCase returns flow and if collects IResult Success then lastSearchQuery will be updated with searched query when invoke the search fun`() =
        runTest {
            // given
            val mockQuery = "abc"
            val mockTotal = Random.nextInt()
            val mockList: List<Product> = listOf(mockk())
            val mockProductResponse = ProductsResponse(
                Random.nextInt(),
                mockList,
                Random.nextInt(),
                mockTotal
            )

            // when
            every { searchProductUseCase(any(), any(), any()) } answers {
                flowOf(
                    IResult.Success(
                        mockProductResponse
                    )
                )
            }

            sut.search(mockQuery)
            advanceUntilIdle()

            // then
            assertTrue(sut.lastSearchQuery == mockQuery)

        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `searchProductUseCase returns flow and if collects IResult Error then error of UiState updates with ResultError when invoke the search fun`() =
        runTest {
            // given
            val mockQuery = "abc"
            val mockResultError = ResultError("")

            // when
            every {
                searchProductUseCase(
                    any(),
                    any(),
                    any()
                )
            } answers { flowOf(IResult.Error(mockResultError)) }
            sut.search(mockQuery)
            advanceUntilIdle()

            // then
            assertTrue(sut.state.value.error == mockResultError)
        }

    @Test
    fun `searchProductUseCase returns flow and if collects IResult Error then isLoading of UiState updated as false when invoke the search fun`() =
        runTest {
            // given
            val mockQuery = "abc"
            val mockResultError = ResultError("")

            // when
            every {
                searchProductUseCase(
                    any(),
                    any(),
                    any()
                )
            } answers { flowOf(IResult.Error(mockResultError)) }
            sut.search(mockQuery)

            // then
            assertTrue(!sut.state.value.isLoading)
        }

}