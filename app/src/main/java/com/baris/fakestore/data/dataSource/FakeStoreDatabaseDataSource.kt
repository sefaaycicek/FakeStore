package com.baris.fakestore.data.dataSource

import com.baris.fakestore.data.source.local.entity.BasketProductEntity
import com.baris.fakestore.data.source.local.entity.FavoriteProductEntity

/**
 * Created on 26.02.2024.
 * @author saycicek
 */
interface FakeStoreDatabaseDataSource {

    suspend fun getBasketProducts(): List<BasketProductEntity>

    suspend fun getBasketProductById(id: Int): BasketProductEntity?

    suspend fun addBasketProduct(basketProductEntity: BasketProductEntity)

    suspend fun deleteBasketProduct(id: Int)

    suspend fun deleteBasket()

    suspend fun updateBasketProductQuantity(basketProductEntity: BasketProductEntity)

    suspend fun getFavoriteProducts(): List<FavoriteProductEntity>

    suspend fun getFavoriteProductById(id: Int): FavoriteProductEntity?

    suspend fun addFavoriteProduct(favoriteProductEntity: FavoriteProductEntity)

    suspend fun deleteFavoriteProduct(id: Int)

}