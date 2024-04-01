package com.baris.fakestore.domain.usecase

import com.baris.fakestore.core.IResult
import com.baris.fakestore.domain.model.ProductsResponse
import com.baris.fakestore.domain.repository.FakeStoreRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.io.IOException

/**
 * Created on 28.02.2024.
 * @author saycicek
 */
class GetAllProductsUseCaseTest {

    private lateinit var repository: FakeStoreRepository
    private lateinit var sut: GetAllProductsUseCase

    private val limit = 20
    private val skip = 0

    @Before
    fun setup() {
        repository = mockk(relaxed = true)
        sut = GetAllProductsUseCase(repository)
    }

    @Test
    fun `invoke getProducts fun in FakeStoreRepository when execute the use case`() =
        runTest {
            //when
            sut.invoke(limit, skip).onCompletion {
                coVerify(exactly = 1) { repository.getProducts(any(), any()) }
            }
        }

    @Test
    fun `emits IResult Loading as first value when execute the use case`() = runTest {

        //when
        coEvery { repository.getProducts(any<Int>(), any<Int>()) }
        val result = sut.invoke(limit, skip)

        //then
        Assert.assertTrue(result.first() is IResult.Loading)
    }

    @Test
    fun `emits IResult Error when execute the use case when throws an exception`() = runTest {

        //when
        coEvery { repository.getProducts(any<Int>(), any<Int>()) } throws IOException()
        val result = sut.invoke(limit, skip)

        //then
        Assert.assertTrue(result.last() is IResult.Error)
    }

    @Test
    fun `emits IResult Success when execute the use case without problem`() = runTest {

        val productsResponse: ProductsResponse = mockk(relaxed = true)

        //when
        coEvery { repository.getProducts(any<Int>(), any<Int>()) } returns productsResponse
        val result = sut.invoke(limit, skip)

        //then
        Assert.assertTrue(result.last() is IResult.Success)
    }

}