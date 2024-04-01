package com.baris.fakestore.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created on 26.02.2024.
 * @author saycicek
 */

@Entity("basket_products")
data class BasketProductEntity(
    @PrimaryKey
    val productId: Int,
    val quantity: Int
)