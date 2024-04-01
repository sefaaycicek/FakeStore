package com.baris.fakestore.domain.usecase

import com.baris.fakestore.core.IResult
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
import kotlin.random.Random

/**
 * Created on 28.02.2024.
 * @author saycicek
 */
class DeleteProductFromBasketUseCaseTest {

    private lateinit var repository: FakeStoreRepository
    private lateinit var sut: DeleteProductFromBasketUseCase

    private val productId: Int = Random.nextInt(1, 100)

    @Before
    fun setup() {
        repository = mockk(relaxed = true)
        sut = DeleteProductFromBasketUseCase(repository)
    }

    @Test
    fun `invoke deleteProductFromBasket fun in FakeStoreRepository when execute the use case`() =
        runTest {
            //when
            sut.invoke(productId).onCompletion {
                coVerify(exactly = 1) { repository.deleteProductFromBasket(any()) }
            }
        }

    @Test
    fun `emits IResult Loading as first value when execute the use case`() = runTest {

        //when
        coEvery { repository.deleteProductFromBasket(any<Int>()) }
        val result = sut.invoke(productId)

        //then
        Assert.assertTrue(result.first() is IResult.Loading)
    }

    @Test
    fun `emits IResult Error when execute the use case when throws an exception`() = runTest {

        //when
        coEvery { repository.deleteProductFromBasket(any<Int>()) } throws IOException()
        val result = sut.invoke(productId)

        //then
        Assert.assertTrue(result.last() is IResult.Error)
    }

    @Test
    fun `emits IResult Success when execute the use case without problem`() = runTest {

        //when
        coEvery { repository.deleteProductFromBasket(any<Int>()) } returns Unit
        val result = sut.invoke(productId)

        //then
        Assert.assertTrue(result.last() is IResult.Success)
    }

}