package com.baris.fakestore.data.source.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.baris.fakestore.data.source.local.entity.BasketProductEntity

/**
 * Created on 26.02.2024.
 * @author saycicek
 */

@Dao
interface BasketProductsDao {

    @Query("select * from basket_products")
    suspend fun getBasketProducts(): List<BasketProductEntity>

    @Query("select * from basket_products where productId = :id")
    suspend fun getBasketProductById(id: Int): BasketProductEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addBasketProduct(basketProductEntity: BasketProductEntity)

    @Query("delete from basket_products where productId = :id")
    suspend fun deleteBasketProductById(id: Int)

    @Query("delete from basket_products")
    suspend fun deleteBasket()

    @Update
    suspend fun updateBasketProductQuantity(basketProductEntity: BasketProductEntity)
}