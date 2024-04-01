package com.baris.fakestore.ui.feature.favorites

import com.baris.fakestore.core.IResult
import com.baris.fakestore.domain.model.Product
import com.baris.fakestore.domain.usecase.AddToBasketUseCase
import com.baris.fakestore.domain.usecase.DeleteFavoriteProductUseCase
import com.baris.fakestore.domain.usecase.GetFavoriteProductsUseCase
import com.baris.fakestore.domain.usecase.UpdateBasketQuantityUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

/**
 * Created on 29.02.2024.
 * @author saycicek
 */
class FavoriteViewModelTest {

    private lateinit var sut: FavoriteViewModel
    private lateinit var getFavoriteProductsUseCase: GetFavoriteProductsUseCase
    private lateinit var deleteFavoriteProductsUseCase: DeleteFavoriteProductUseCase
    private lateinit var addToBasketUseCase: AddToBasketUseCase
    private lateinit var updateBasketQuantityUseCase: UpdateBasketQuantityUseCase

    @OptIn(ExperimentalCoroutinesApi::class)
    private val dispatcher = UnconfinedTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        getFavoriteProductsUseCase = mockk(relaxed = true)
        deleteFavoriteProductsUseCase = mockk(relaxed = true)
        addToBasketUseCase = mockk(relaxed = true)
        updateBasketQuantityUseCase = mockk(relaxed = true)
        sut = FavoriteViewModel(getFavoriteProductsUseCase, deleteFavoriteProductsUseCase, addToBasketUseCase, updateBasketQuantityUseCase)
        Dispatchers.setMain(dispatcher)
    }


    @Test
    fun `invokes getFavoriteProductsUseCase when invoke the getFavorites fun`() = runTest {
        // when
        sut.getFavorites()
        // then
        verify(exactly = 1) { getFavoriteProductsUseCase() }
    }

    @Test
    fun `invokes deleteFavoriteProductsUseCase when invoke the removeFavorite fun`() = runTest {
        //given
        val mockId = 1

        // when
        sut.removeFavorite(mockId)
        // then
        verify(exactly = 1) { deleteFavoriteProductsUseCase(any()) }
    }

    @Test
    fun `invokes addToBasketUseCase if quantity is 0 when invoke the onIncreaseBasketQuantity fun`() = runTest {
        //given
        val mockId = 1
        val mockQuantity = 0
        // when
        sut.onIncreaseBasketQuantity(mockId, mockQuantity)
        // then
        verify(exactly = 1) { addToBasketUseCase(any()) }
    }

    @Test
    fun `invokes updateBasketQuantityUseCase if quantity is not 0 when invoke the onIncreaseBasketQuantity fun`() = runTest {
        //given
        val mockId = 1
        val mockQuantity = 1
        // when
        sut.onIncreaseBasketQuantity(mockId, mockQuantity)
        // then
        verify(exactly = 1) { updateBasketQuantityUseCase(any(), any()) }
    }

    @Test
    fun `invokes updateBasketQuantityUseCase when invoke the onDecreaseBasketQuantity fun`() = runTest {
        //given
        val mockId = 1
        val mockQuantity = 0
        // when
        sut.onDecreaseBasketQuantity(mockId, mockQuantity)
        // then
        verify(exactly = 1) { updateBasketQuantityUseCase(any(), any()) }
    }

    @Test
    fun `updates data of UiState if getFavoriteProductsUseCase collects IResult Success when invoke the getFavorites fun`() = runTest {
        //given
        val mockResponse: List<Product> = listOf(mockk())
        // when
        every { getFavoriteProductsUseCase() } answers { flowOf(IResult.Success(mockResponse)) }
        sut.getFavorites()

        // then
        assertTrue(sut.state.value.data == mockResponse)
    }

    @Test
    fun `invokes getFavorites fun if deleteFavoriteProductUseCase collects IResult Success when invoke the removeFavorite fun`() = runTest {
        // when
        every { deleteFavoriteProductsUseCase(any()) } answers { flowOf(IResult.Success(Unit)) }
        sut.getFavorites()

        // then
        verify(exactly = 1) { getFavoriteProductsUseCase() }
    }

}