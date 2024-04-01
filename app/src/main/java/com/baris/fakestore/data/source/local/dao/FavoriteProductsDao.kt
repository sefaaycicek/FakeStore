package com.baris.fakestore.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.baris.fakestore.data.source.local.entity.FavoriteProductEntity

/**
 * Created on 26.02.2024.
 * @author saycicek
 */

@Dao
interface FavoriteProductsDao {

    @Query("select * from favorite_products")
    suspend fun getFavoriteProducts(): List<FavoriteProductEntity>

    @Query("select * from favorite_products where productId = :id")
    suspend fun getFavoriteProductById(id: Int): FavoriteProductEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavoriteProduct(favoriteProductEntity: FavoriteProductEntity)

    @Query("delete from favorite_products where productId = :id")
    suspend fun deleteFavoriteProductById(id: Int)

}