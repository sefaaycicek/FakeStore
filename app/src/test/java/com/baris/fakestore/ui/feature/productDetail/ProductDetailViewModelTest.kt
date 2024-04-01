package com.baris.fakestore.ui.feature.productDetail

import com.baris.fakestore.core.IResult
import com.baris.fakestore.domain.model.Product
import com.baris.fakestore.domain.usecase.AddFavoriteProductUseCase
import com.baris.fakestore.domain.usecase.AddToBasketUseCase
import com.baris.fakestore.domain.usecase.DeleteFavoriteProductUseCase
import com.baris.fakestore.domain.usecase.GetProductUseCase
import com.baris.fakestore.domain.usecase.UpdateBasketQuantityUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert
import org.junit.Before
import org.junit.Test

/**
 * Created on 29.02.2024.
 * @author saycicek
 */
class ProductDetailViewModelTest {

    private lateinit var getProductUseCase: GetProductUseCase
    private lateinit var addToBasketUseCase: AddToBasketUseCase
    private lateinit var updateBasketQuantityUseCase: UpdateBasketQuantityUseCase
    private lateinit var addFavoriteProductUseCase: AddFavoriteProductUseCase
    private lateinit var deleteFavoriteProductUseCase: DeleteFavoriteProductUseCase
    private lateinit var sut: ProductDetailViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    private val dispatcher = UnconfinedTestDispatcher()

    private val mockProductId = 1

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        getProductUseCase = mockk(relaxed = true)
        addToBasketUseCase = mockk(relaxed = true)
        updateBasketQuantityUseCase = mockk(relaxed = true)
        addFavoriteProductUseCase = mockk(relaxed = true)
        deleteFavoriteProductUseCase = mockk(relaxed = true)
        sut = ProductDetailViewModel(getProductUseCase, addToBasketUseCase, updateBasketQuantityUseCase, addFavoriteProductUseCase, deleteFavoriteProductUseCase)
        Dispatchers.setMain(dispatcher)
    }

    @Test
    fun `invokes getFavoriteProductsUseCase when invoke the getProductById fun`() = runTest {
        // when
        sut.getProductById(mockProductId)
        // then
        verify(exactly = 1) { getProductUseCase(any()) }
    }

}