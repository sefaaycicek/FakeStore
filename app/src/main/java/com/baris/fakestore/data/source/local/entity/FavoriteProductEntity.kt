package com.baris.fakestore.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created on 26.02.2024.
 * @author saycicek
 */

@Entity("favorite_products")
data class FavoriteProductEntity(
    @PrimaryKey
    val productId: Int
)
