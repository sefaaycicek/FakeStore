package com.baris.fakestore.data.dataSource

import com.baris.fakestore.data.source.local.dao.BasketProductsDao
import com.baris.fakestore.data.source.local.dao.FavoriteProductsDao
import com.baris.fakestore.data.source.local.entity.BasketProductEntity
import com.baris.fakestore.data.source.local.entity.FavoriteProductEntity
import javax.inject.Inject

/**
 * Created on 26.02.2024.
 * @author saycicek
 */
class FakeStoreDatabaseDataSourceImpl @Inject constructor(
    private val basketProductsDao: BasketProductsDao,
    private val favoriteProductsDao: FavoriteProductsDao
) : FakeStoreDatabaseDataSource {
    override suspend fun getBasketProducts(): List<BasketProductEntity> {
        return basketProductsDao.getBasketProducts()
    }

    override suspend fun getBasketProductById(id: Int): BasketProductEntity? {
        return basketProductsDao.getBasketProductById(id)
    }

    override suspend fun addBasketProduct(basketProductEntity: BasketProductEntity) {
        return basketProductsDao.addBasketProduct(basketProductEntity)
    }

    override suspend fun deleteBasketProduct(id: Int) {
        return basketProductsDao.deleteBasketProductById(id)
    }

    override suspend fun deleteBasket() {
        return basketProductsDao.deleteBasket()
    }

    override suspend fun updateBasketProductQuantity(basketProductEntity: BasketProductEntity) {
        return basketProductsDao.updateBasketProductQuantity(basketProductEntity)
    }

    override suspend fun getFavoriteProducts(): List<FavoriteProductEntity> {
        return favoriteProductsDao.getFavoriteProducts()
    }

    override suspend fun getFavoriteProductById(id: Int): FavoriteProductEntity? {
        return favoriteProductsDao.getFavoriteProductById(id)
    }

    override suspend fun addFavoriteProduct(favoriteProductEntity: FavoriteProductEntity) {
        return favoriteProductsDao.addFavoriteProduct(favoriteProductEntity)
    }

    override suspend fun deleteFavoriteProduct(id: Int) {
        return favoriteProductsDao.deleteFavoriteProductById(id)
    }
}