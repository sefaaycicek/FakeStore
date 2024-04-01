package com.baris.fakestore.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.baris.fakestore.data.source.local.dao.BasketProductsDao
import com.baris.fakestore.data.source.local.dao.FavoriteProductsDao
import com.baris.fakestore.data.source.local.entity.BasketProductEntity
import com.baris.fakestore.data.source.local.entity.FavoriteProductEntity

/**
 * Created on 26.02.2024.
 * @author saycicek
 */

@Database(
    entities = [
        BasketProductEntity::class,
        FavoriteProductEntity::class
    ],
    version = 1
)
abstract class FakeStoreDatabase : RoomDatabase() {

    companion object {
        const val NAME = "fake_store.db"
    }

    abstract fun getBasketProductsDao(): BasketProductsDao

    abstract fun getFavoriteProductsDao(): FavoriteProductsDao

}